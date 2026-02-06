package com.example.service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.config.JwtService;
import com.example.entity.RefreshToken;
import com.example.entity.UserInformation;
import com.example.exception.JwtAuthException;
import com.example.repository.RefreshTokenRepository;
import com.example.repository.UserInformationRepository;

import jakarta.transaction.Transactional;

@Service
public class RefreshTokenService {
	
	

	    @Value("${jwt.refreshExpirationMs}")
	    private Long refreshTokenDurationMs;

	    private final RefreshTokenRepository refreshTokenRepository;
	    private final UserInformationRepository userInformationRepository;
	    private final JwtService jwtService;

	    public RefreshTokenService( RefreshTokenRepository refreshTokenRepository,UserInformationRepository userInformationRepository,JwtService jwtService) {

	        this.refreshTokenRepository = refreshTokenRepository;
	        this.userInformationRepository = userInformationRepository;
	        this.jwtService = jwtService;
	    }

	    
	    
	    @Transactional
	    public RefreshToken createRefreshToken(Long userId) {
	    	UserInformation user = userInformationRepository.findById(userId)
	                .orElseThrow(() -> new RuntimeException("User not found"));

	    	
	    	refreshTokenRepository.deleteByUser(user);
	    	String token;
	        do {
	            token = UUID.randomUUID().toString();
	        } while (refreshTokenRepository.findByRefreshToken(token).isPresent());


	        RefreshToken refreshToken = new RefreshToken();
	        refreshToken.setUser(userInformationRepository.findById(userId).orElseThrow());
	        refreshToken.setRefreshToken(UUID.randomUUID().toString());
	        refreshToken.setExpiryDate(
	                LocalDateTime.now().plusSeconds(refreshTokenDurationMs / 1000)
	        );

	        refreshToken.setCreatedBy(userId);
	        return refreshTokenRepository.save(refreshToken);
	    }
	    
	    
	    
	    @Transactional
	    public Map<String, String> refreshToken(String refreshToken) {
	    	if (refreshToken == null || refreshToken.isBlank()) {
	            throw new JwtAuthException(
	                    "REFRESH TOKEN MISSING",
	                    "refresh token is missing"
	            );
	        }
	    	 
	        RefreshToken token = refreshTokenRepository.findByRefreshToken(refreshToken).orElseThrow(() -> 
	        new JwtAuthException("REFRESH TOKEN INVALID", "refresh token is invalid")
	                );
	        if (token.getExpiryDate().isBefore(LocalDateTime.now())) 
	        {
	            refreshTokenRepository.delete(token);
	            throw new JwtAuthException("REFRESH TOKEN EXPIRED", "refresh token expired");
	        }

	        UserInformation user = token.getUser();

	        
	        refreshTokenRepository.delete(token);
	        
	        RefreshToken newToken = createRefreshToken(user.getId());

	        String accessToken =jwtService.generateAccessToken(user.getUsername(),user.getRoles());
	        
	       

	        
	        return Map.of(
	                "accessToken", accessToken,
	                "refreshToken", newToken.getRefreshToken()
	        );
	    }
	}