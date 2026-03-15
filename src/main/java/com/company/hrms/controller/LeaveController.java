package com.company.hrms.controller;

import com.company.hrms.entity.LeaveRequest;
import com.company.hrms.entity.LeaveType;
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

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_EMPLOYEE')")
    public LeaveRequest requestLeave(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            @RequestParam LeaveType type,
            Authentication auth) {
        return leaveService.requestLeave(
                auth.getName(),
                startDate,
                endDate,    // was startDate (bug)
                type
        );
    }

    @PostMapping("/{id}/approve")
    @PreAuthorize("hasAuthority('ROLE_MANAGER') or hasAuthority('ROLE_ADMIN')")
    public void approveLeave(@PathVariable Long id) {
        leaveService.approveLeave(id);
    }
}