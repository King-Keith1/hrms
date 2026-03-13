package com.company.hrms.controller;

import com.company.hrms.dto.CreateEmployeeRequest;
import com.company.hrms.entity.Employee;
import com.company.hrms.service.EmployeeService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    //@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public Employee createEmployee(
            @RequestBody CreateEmployeeRequest request,
            Authentication authentication) {

        return employeeService.createEmployee(
                request,
                authentication.getName()
        );
    }
}