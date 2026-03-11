package com.company.hrms.service;

import com.company.hrms.dto.*;
import com.company.hrms.entity.*;
import com.company.hrms.repository.*;
import com.company.hrms.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
                       DepartmentRepository departmentRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByUsername(request.username())) {
            throw new RuntimeException("Username already exists");
        }

        Department department = departmentRepository
                .findById(request.departmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        User user = new User(
                request.username(),
                passwordEncoder.encode(request.password()),
                Role.valueOf(request.role()),
                department
        );

        userRepository.save(user);

        String token = jwtService.generateToken(user);
        return new AuthResponse(token, "Bearer");
    }

    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByUsernameIgnoreCase(request.username())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtService.generateToken(user);
        return new AuthResponse(token, "Bearer");
    }
}