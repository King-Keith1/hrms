package com.company.hrms.controller;

import com.company.hrms.dto.PayslipResponse;
import com.company.hrms.entity.Employee;
import com.company.hrms.repository.EmployeeRepository;
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
    @PreAuthorize("hasRole('HR_MANAGER')")
    public PayslipResponse generate(
            @PathVariable Long employeeId,
            @RequestParam YearMonth month) {

        return payrollService.generatePayroll(employeeId, month);
    }
}