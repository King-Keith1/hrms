package com.company.hrms.dto;

import java.math.BigDecimal;
import java.time.YearMonth;

public record PayslipResponse(
        String employeeName,
        YearMonth month,
        BigDecimal grossPay
) {}
