package com.company.hrms.dto;

import java.math.BigDecimal;

public record CreateEmployeeRequest(
        String fullName,
        String employeeNumber,
        String department,
        BigDecimal hourlyRate,
        Long userId
) {}
