package com.example.config;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.exception.JwtAuthException;
import com.example.service.UserInformationDetailsService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

	private final UserInformationDetailsService employeeUserDetailsService;
	private final JwtService jwtService;
	private final JwtAuthenticationEntryPoint entryPoint;

	public JwtAuthFilter(UserInformationDetailsService employeeUserDetailsService, JwtService jwtService,
			JwtAuthenticationEntryPoint entryPoint) {
		this.employeeUserDetailsService = employeeUserDetailsService;
		this.jwtService = jwtService;
		this.entryPoint = entryPoint;
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		String path = request.getServletPath();
		return path.equals("/auth/logout") 
				|| path.equals("/auth/login") 
				|| path.equals("/auth/refresh")
				|| path.equals("/auth/welcome");
	}
	
	
	@Override
	protected void doFilterInternal(HttpServletRequest request,
	                                HttpServletResponse response,
	                                FilterChain filterChain)
	        throws ServletException, IOException {

	    try {

	        String authHeader = request.getHeader("Authorization");

	        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
	            throw new JwtAuthException("TOKEN_MISSING", "token is missing");
	        }

	        String token = authHeader.substring(7);
	        
	        String username = jwtService.extractUsername(token);

	        if (username != null &&
	            SecurityContextHolder.getContext().getAuthentication() == null) {

	            UserDetails userDetails =
	                    employeeUserDetailsService.loadUserByUsername(username);

	            jwtService.validateToken(token, userDetails);

	            UsernamePasswordAuthenticationToken authToken =
	                    new UsernamePasswordAuthenticationToken(
	                            userDetails,
	                            null,
	                            userDetails.getAuthorities());

	            authToken.setDetails(
	                    new WebAuthenticationDetailsSource().buildDetails(request)
	            );

	            SecurityContextHolder.getContext().setAuthentication(authToken);
	        }

	        filterChain.doFilter(request, response);

	    } /*catch (JwtAuthException ex) {
	        SecurityContextHolder.clearContext();
	        entryPoint.commence(request, response, ex);*/
	    catch (ExpiredJwtException ex) {
	        SecurityContextHolder.clearContext();
	        entryPoint.commence(
	            request,
	            response,
	            new JwtAuthException("TOKEN_EXPIRED", "token is expired")
	        );
	        return;
	    }
	    catch (JwtException ex) {
	        SecurityContextHolder.clearContext();
	        entryPoint.commence(
	            request,
	            response,
	            new JwtAuthException("TOKEN_INVALID", "token is invalid")
	        );
	        return;
	    }
	    catch (JwtAuthException ex) {
	        SecurityContextHolder.clearContext();
	        entryPoint.commence(request, response, ex);
	        return;
	    }

	    }
	


}