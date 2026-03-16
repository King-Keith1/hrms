package com.company.hrms.controller;

import com.company.hrms.dto.AuthResponse;
import com.company.hrms.dto.LoginRequest;
import com.company.hrms.dto.RegisterRequest;
import com.company.hrms.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth") // Base endpoint for authentication operations
public class AuthController {

    private final AuthService authService;

    // Constructor injection of authentication service
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Registers a new user and returns a JWT authentication response
    @PostMapping("/register")
    public AuthResponse register(@RequestBody @Valid RegisterRequest request) {
        return authService.register(request);
    }

    // Authenticates a user and returns a JWT token
    @PostMapping("/login")
    public AuthResponse login(@RequestBody @Valid LoginRequest request) {
        return authService.login(request);
    }
}