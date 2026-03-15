package com.company.hrms.controller;

import com.company.hrms.dto.PayslipResponse;
import com.company.hrms.service.PayrollService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;

@RestController
@RequestMapping("/payroll")
public class PayrollController {

    private final PayrollService payrollService;

    public PayrollController(PayrollService payrollService) {
        this.payrollService = payrollService;
    }

    @PostMapping("/generate/{employeeId}")
    @PreAuthorize("hasAuthority('ROLE_MANAGER') or hasAuthority('ROLE_ADMIN')")
    public PayslipResponse generate(
            @PathVariable Long employeeId,
            @RequestParam YearMonth month) {
        return payrollService.generatePayroll(employeeId, month);
    }
}