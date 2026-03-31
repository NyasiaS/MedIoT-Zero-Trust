package com.nyasia.controlplane.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain controlPlaneSecurity(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**", "/h2/**").permitAll()
                        .anyRequest().permitAll()
                )
                // If later you want CP endpoints protected too, switch to:
                // .oauth2ResourceServer(oauth2 -> oauth2.jwt())
                .headers(h -> h.frameOptions(f -> f.sameOrigin())) // H2 console
                .build();
    }
}