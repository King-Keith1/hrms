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

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @PostMapping("/mark")
    public void markAttendance(@RequestParam int hoursWorked,
                               Authentication authentication) {

        attendanceService.markAttendance(authentication.getName(), hoursWorked);
    }
}
