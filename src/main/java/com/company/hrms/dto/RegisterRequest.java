package com.company.hrms.dto;

import jakarta.validation.constraints.*;

public record RegisterRequest(

        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 50)
        String username,

        @NotBlank(message = "Password is required")
        @Size(min = 6, max = 100)
        String password,

        @NotBlank(message = "Role is required")
        String role,

        @NotNull(message = "Department ID is required")
        Long departmentId
) {}