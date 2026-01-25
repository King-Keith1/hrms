package com.company.hrms.dto;

public record RegisterRequest(
        String username,
        String password,
        String role
) {}
