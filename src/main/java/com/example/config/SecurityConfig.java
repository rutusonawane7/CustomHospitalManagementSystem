package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.service.UserInformationDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	private final JwtAuthFilter jwtAuthFilter;
	
	public SecurityConfig(JwtAuthFilter jwtAuthFilter){
		
		this.jwtAuthFilter = jwtAuthFilter;
		
		
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http,
			AuthenticationProvider authenticationProvider) throws Exception{
		http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(
                "/auth/welcome",
                "/auth/generateToken",
                "/auth/login",
                "/auth/logout",
                "/auth/refresh"
            ).permitAll()
            .anyRequest().authenticated()
        )
        .sessionManagement(sess ->
            sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )
        .authenticationProvider(authenticationProvider)
        .exceptionHandling(exception -> 
        exception.authenticationEntryPoint(new JwtAuthenticationEntryPoint()) // <-- here
    )
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
}

	
	
	
	  
	    @Bean
	    public AuthenticationProvider authenticationProvider(UserInformationDetailsService employeeUserDetailsService,
	            PasswordEncoder passwordEncoder) {
	        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
	        provider.setUserDetailsService(employeeUserDetailsService);
	        provider.setPasswordEncoder(passwordEncoder);
	        provider.setHideUserNotFoundExceptions(false);
	        return provider;
	    }
	    
	    @Bean
	    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
	        return config.getAuthenticationManager();
	    }
	
}
