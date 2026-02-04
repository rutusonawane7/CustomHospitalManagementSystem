package com.example.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.RefreshToken;
import com.example.entity.UserInformation;

import jakarta.transaction.Transactional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
	Optional<RefreshToken> findByRefreshToken(String refreshtoken);

	//@Transactional
    void deleteByUser(UserInformation user);

	  
	
}
