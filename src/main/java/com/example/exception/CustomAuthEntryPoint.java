package com.example.exception;

import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			org.springframework.security.core.AuthenticationException authException)
			throws java.io.IOException, ServletException {
		
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        response.getWriter().write("""
        {
            "status": 401,
            "code": "UNAUTHORIZED",
            "message": "Authentication is required"
        }
        """);
    }
		
}
   
