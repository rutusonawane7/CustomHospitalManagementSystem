package com.example.config;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.service.UserInformationDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
	
	 private final UserInformationDetailsService employeeUserDetailsService;
	    private final JwtService jwtService;

	 public JwtAuthFilter(UserInformationDetailsService employeeUserDetailsService, JwtService jwtService) {
		 this.employeeUserDetailsService=employeeUserDetailsService;
		 this.jwtService=jwtService;
	 }
	 
	
	 /*@Override
	 protected boolean shouldNotFilter(HttpServletRequest request) {
	     return request.getServletPath().equals("/auth/login");
	 }

	@Override
	protected void doFilterInternal(HttpServletRequest request, 
			HttpServletResponse response, 
			FilterChain filterChain)
			throws ServletException, IOException {
		
		String authHeader = request.getHeader("Authorization");
		String token = null;
		String username = null;
		
		
		if(authHeader != null && authHeader.startsWith("Bearer ")) {
			token = authHeader.substring(7).trim();
			username = jwtService.extractUsername(token);
		}
		
		if(username != null &&
			SecurityContextHolder.getContext().getAuthentication()==null) {
			
			UserDetails userDetails =
			        employeeUserDetailsService.loadUserByUsername(username);
			if (jwtService.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                		userDetails,
                        null,
                        userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
   
		
	}
*/
	 
	 @Override
	    protected boolean shouldNotFilter(HttpServletRequest request) {
	        String path = request.getServletPath();
	        return path.equals("/auth/logout")
	            || path.equals("/auth/login")
	            || path.equals("/auth/refresh")
	            || path.equals("/auth/welcome");
	    }

	    @Override
	    protected void doFilterInternal(
	            HttpServletRequest request,
	            HttpServletResponse response,
	            FilterChain filterChain)
	            throws ServletException, IOException {

	        String authHeader = request.getHeader("Authorization");

	        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
	            filterChain.doFilter(request, response);
	            return;
	        }

	        String token = authHeader.substring(7);

	        try {
	            String username = jwtService.extractUsername(token);

	            if (username != null &&
	                SecurityContextHolder.getContext().getAuthentication() == null) {

	                UserDetails userDetails =
	                        employeeUserDetailsService.loadUserByUsername(username);

	                if (jwtService.validateToken(token, userDetails)) {
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
	            }

	        } catch (Exception e) {
	            // Let AuthenticationEntryPoint handle expired/invalid token
	            SecurityContextHolder.clearContext();
	        }

	        filterChain.doFilter(request, response);
	    }
	
}
