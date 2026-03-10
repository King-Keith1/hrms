package com.company.hrms.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record CreateEmployeeRequest(

        @NotBlank(message = "Full name is required")
        @Size(max = 100)
        String fullName,

        @NotBlank(message = "Employee number is required")
        @Size(max = 20)
        String employeeNumber,

        @NotNull(message = "Department ID is required")
        Long departmentId,

        @NotNull(message = "Hourly rate is required")
        @DecimalMin(value = "0.0", inclusive = false)
        BigDecimal hourlyRate
) {}