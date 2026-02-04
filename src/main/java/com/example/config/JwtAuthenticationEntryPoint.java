package com.example.config;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.example.exception.JwtAuthException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint{

	@Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException {

        String code = "UNAUTHORIZED";
        String message = authException.getMessage();

        if (authException instanceof JwtAuthException jwtEx) {
            code = jwtEx.getCode();
        }

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");

        response.getWriter().write(
            "{ \"status\": 401, \"code\": \"" + code +
            "\", \"message\": \"" + message + "\" }"
        );
    }
}