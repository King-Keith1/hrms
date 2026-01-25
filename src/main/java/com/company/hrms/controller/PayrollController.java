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
    private final EmployeeRepository employeeRepository;

    public PayrollController(PayrollService payrollService,
                             EmployeeRepository employeeRepository) {
        this.payrollService = payrollService;
        this.employeeRepository = employeeRepository;
    }

    // HR generates payroll
    @PreAuthorize("hasRole('HR_MANAGER')")
    @PostMapping("/generate/{employeeId}")
    public PayslipResponse generate(
            @PathVariable Long employeeId,
            @RequestParam YearMonth month) {

        Employee employee = employeeRepository.findById(employeeId).orElseThrow();

        var payroll = payrollService.generatePayroll(employee, month);

        return new PayslipResponse(
                employee.getFullName(),
                payroll.getMonth(),
                payroll.getGrossPay()
        );
    }
}
