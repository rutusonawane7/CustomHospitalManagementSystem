package com.example.config;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.example.service.UserInformationDetails;

@Configuration
@EnableJpaAuditing
public class JpaAuditConfig {
	@Bean
    public AuditorAware<Long> auditorProvider() {
        return () -> {
            Authentication auth =
                    SecurityContextHolder.getContext().getAuthentication();

            if (auth == null || !auth.isAuthenticated()
                    || auth.getPrincipal().equals("anonymousUser")) {
                return Optional.empty();
            }

            UserInformationDetails user =
                    (UserInformationDetails) auth.getPrincipal();

            return Optional.of(user.getId());
        };
    }

}
