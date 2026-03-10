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
                || path.startsWith("/h2-console")
                || path.startsWith("/swagger-ui")
                || path.startsWith("/api-docs");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws java.io.IOException, jakarta.servlet.ServletException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {

            Claims claims = jwtService.extractClaims(token);

            String username = claims.getSubject();
            List<String> permissions = claims.get("permissions", List.class);

            if (permissions == null) {
                permissions = List.of();
            }

            var authorities = permissions.stream()
                    .map(SimpleGrantedAuthority::new)
                    .toList();

            if (username != null &&
                    SecurityContextHolder.getContext().getAuthentication() == null) {

                var auth = new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        authorities
                );

                SecurityContextHolder.getContext().setAuthentication(auth);
            }

        } catch (JwtException e) {
            // Invalid or expired token → ignore and continue
        }

        filterChain.doFilter(request, response);
    }
}