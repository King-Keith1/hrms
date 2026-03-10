package com.company.hrms.controller;

import com.company.hrms.entity.*;
import com.company.hrms.repository.EmployeeRepository;
import com.company.hrms.repository.LeaveRepository;
import com.company.hrms.service.LeaveService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/leaves")
public class LeaveController {

    private final LeaveService leaveService;

    public LeaveController(LeaveService leaveService) {
        this.leaveService = leaveService;
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @PostMapping
    public LeaveRequest requestLeave(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            @RequestParam LeaveType type,
            Authentication auth) {

        return leaveService.requestLeave(
                auth.getName(),
                startDate,
                endDate,
                type
        );
    }

    @PreAuthorize("hasRole('HR_MANAGER')")
    @PostMapping("/{id}/approve")
    public void approveLeave(@PathVariable Long id) {
        leaveService.approveLeave(id);
    }
}