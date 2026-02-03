package com.example.config;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtService {



	    public static final String SECRET =
	            "5367566859703373367639792F423F452848284D6251655468576D5A71347437";

	    private static final long ACCESS_TOKEN_EXPIRY = 1000 * 60 * 1; // 1 minute

	    public String generateAccessToken(String email, String role) {

	        Map<String, Object> claims = new HashMap<>();
	        claims.put("role", role);
	        claims.put("type", "ACCESS"); // 🔐 VERY IMPORTANT

	        return Jwts.builder()
	                .setClaims(claims)
	                .setSubject(email)
	                .setIssuedAt(new Date())
	                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRY))
	                .signWith(getSignKey(), SignatureAlgorithm.HS256)
	                .compact();
	    }

	    public String extractUsername(String token) {
	        return extractAllClaims(token).getSubject();
	    }

	    public boolean isAccessToken(String token) {
	        return "ACCESS".equals(extractAllClaims(token).get("type"));
	    }

	    public boolean isTokenExpired(String token) {
	        return extractAllClaims(token).getExpiration().before(new Date());
	    }

	    public boolean validateToken(String token, UserDetails userDetails) {
	        return extractUsername(token).equals(userDetails.getUsername())
	                && !isTokenExpired(token)
	                && isAccessToken(token);
	    }

	    private Claims extractAllClaims(String token) {
	        return Jwts.parserBuilder()
	                .setSigningKey(getSignKey())
	                .build()
	                .parseClaimsJws(token)
	                .getBody();
	    }

	    private Key getSignKey() {
	        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
	        return Keys.hmacShaKeyFor(keyBytes);
	    }
	}
