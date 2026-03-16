package com.company.hrms.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configures Spring Security for the application.
 * Uses JWT-based authentication and stateless sessions.
 */
@Configuration
@EnableMethodSecurity(prePostEnabled = true) // Enables @PreAuthorize/@PostAuthorize annotations
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter; // Custom JWT filter

    public SecurityConfig(JwtAuthenticationFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    /**
     * Defines the main security filter chain.
     *
     * @param http HttpSecurity builder
     * @return configured SecurityFilterChain
     * @throws Exception in case of config errors
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF for stateless JWT-based API
                .csrf(csrf -> csrf.disable())

                // Configure public and protected endpoints
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/auth/**",           // Authentication endpoints
                                "/swagger-ui/**",     // Swagger UI
                                "/swagger-ui.html",
                                "/api-docs",          // OpenAPI docs
                                "/api-docs/**"
                        ).permitAll()              // Publicly accessible
                        .anyRequest().authenticated() // All other endpoints require authentication
                )

                // Add custom JWT filter before Spring Security's username/password filter
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)

                // Configure stateless session (no HTTP session)
                .sessionManagement(sm ->
                        sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        return http.build();
    }
}