package com.example.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

@Entity
public class RefreshToken extends BaseEntity{
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Id;
	
	
	@OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
	private UserInformation user;
	
	private String refreshToken;
	
	private LocalDateTime expiryDate;
	
	private LocalDateTime tokenCreatedAt;

	@PrePersist
    public void onCreate() {
        tokenCreatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        tokenCreatedAt = LocalDateTime.now(); 
    }
	
	
	
	public RefreshToken() {
		
	}
	
	public RefreshToken(Long id, UserInformation user, String refreshToken, LocalDateTime expiryDate) {
		super();
		Id = id;
		this.user = user;
		this.refreshToken = refreshToken;
		this.expiryDate = expiryDate;
	}

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public UserInformation getUser() {
		return user;
	}

	public void setUser(UserInformation user) {
		this.user = user;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public LocalDateTime getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(LocalDateTime instant) {
		this.expiryDate = instant;
	}

	public boolean isExpired() {
        return expiryDate.isBefore(LocalDateTime.now());
    }

	public LocalDateTime getTokenCreatedAt() {
		return tokenCreatedAt;
	}

	public void setTokenCreatedAt(LocalDateTime tokenCreatedAt) {
		this.tokenCreatedAt = tokenCreatedAt;
	}
	
	
	
	
}
