package com.example.service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.config.JwtService;
import com.example.entity.RefreshToken;
import com.example.entity.UserInformation;
import com.example.repository.RefreshTokenRepository;
import com.example.repository.UserInformationRepository;

@Service
public class RefreshTokenService {
	
	

	    @Value("${jwt.refreshExpirationMs}")
	    private Long refreshTokenDurationMs;

	    private final RefreshTokenRepository refreshTokenRepository;
	    private final UserInformationRepository userInformationRepository;
	    private final JwtService jwtService;

	    public RefreshTokenService(
	            RefreshTokenRepository refreshTokenRepository,
	            UserInformationRepository userInformationRepository,
	            JwtService jwtService) {

	        this.refreshTokenRepository = refreshTokenRepository;
	        this.userInformationRepository = userInformationRepository;
	        this.jwtService = jwtService;
	    }

	    public RefreshToken createRefreshToken(Long userId) {

	        RefreshToken token = new RefreshToken();
	        token.setUser(userInformationRepository.findById(userId).orElseThrow());
	        token.setRefreshToken(UUID.randomUUID().toString());
	        token.setExpiryDate(
	                LocalDateTime.now().plusSeconds(refreshTokenDurationMs / 1000)
	        );

	        return refreshTokenRepository.save(token);
	    }

	    public Map<String, String> refreshToken(String refreshToken) {

	        RefreshToken token = refreshTokenRepository
	                .findByRefreshToken(refreshToken)
	                .orElseThrow(() -> new RuntimeException("refresh token expired"));

	        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
	            refreshTokenRepository.delete(token);
	            throw new RuntimeException("refresh token expired");
	        }

	        UserInformation user = token.getUser();

	        // ROTATE refresh token
	        refreshTokenRepository.delete(token);
	        RefreshToken newToken = createRefreshToken(user.getId());

	        String accessToken =
	                jwtService.generateAccessToken(
	                        user.getUsername(),
	                        user.getRoles()
	                );

	        return Map.of(
	                "accessToken", accessToken,
	                "refreshToken", newToken.getRefreshToken()
	        );
	    }
	}