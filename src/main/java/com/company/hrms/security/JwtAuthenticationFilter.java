package com.company.hrms.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.Authentication;

import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/auth")
                || path.startsWith("/swagger-ui")
                || path.startsWith("/api-docs");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws java.io.IOException, jakarta.servlet.ServletException {

        // DEBUG - remove after fixing
        System.out.println("=== JWT FILTER EXECUTING for: " + request.getRequestURI());
        System.out.println("=== Auth header: " + request.getHeader("Authorization"));

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {
            Claims claims = jwtService.extractClaims(token);

            String username = claims.getSubject();
            String role = claims.get("role", String.class);

            Authentication existingAuth = SecurityContextHolder.getContext().getAuthentication();

            if (username != null &&
                    role != null &&
                    (existingAuth == null ||
                            !existingAuth.isAuthenticated() ||
                            existingAuth instanceof org.springframework.security.authentication.AnonymousAuthenticationToken)) {

                var authority = new SimpleGrantedAuthority(role);

                var auth = new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        List.of(authority)
                );

                SecurityContextHolder.getContext().setAuthentication(auth);

                // DEBUG - log successful authentication
                System.out.println("=== JWT Authenticated user: " + username + " with role: " + role);
            }

        } catch (JwtException e) {
            // temporarily log this to see what's failing
            System.out.println("JWT ERROR: " + e.getMessage());
            // invalid token -> request continues without auth
        }

        filterChain.doFilter(request, response);
    }
}