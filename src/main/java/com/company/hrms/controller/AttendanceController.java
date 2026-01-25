package com.company.hrms.controller;

import com.company.hrms.entity.Employee;
import com.company.hrms.repository.EmployeeRepository;
import com.company.hrms.service.AttendanceService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final EmployeeRepository employeeRepository;

    public AttendanceController(AttendanceService attendanceService,
                                EmployeeRepository employeeRepository) {
        this.attendanceService = attendanceService;
        this.employeeRepository = employeeRepository;
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @PostMapping("/mark")
    public void markAttendance(@RequestParam int hoursWorked,
                               Authentication authentication) {

        String username = authentication.getName();

        Employee employee = employeeRepository
                .findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        attendanceService.markAttendance(employee, hoursWorked);
    }
}
