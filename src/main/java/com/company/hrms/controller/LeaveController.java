package com.company.hrms.controller;

import com.company.hrms.entity.*;
import com.company.hrms.repository.EmployeeRepository;
import com.company.hrms.repository.LeaveRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/leaves")
public class LeaveController {

    private final LeaveRepository leaveRepository;
    private final EmployeeRepository employeeRepository;

    public LeaveController(LeaveRepository leaveRepository,
                           EmployeeRepository employeeRepository) {
        this.leaveRepository = leaveRepository;
        this.employeeRepository = employeeRepository;
    }

    // EMPLOYEE requests leave
    @PreAuthorize("hasRole('EMPLOYEE')")
    @PostMapping
    public LeaveRequest requestLeave(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            @RequestParam LeaveType type,
            Authentication auth) {

        Employee employee = employeeRepository
                .findByUserUsername(auth.getName())
                .orElseThrow();

        LeaveRequest leave = new LeaveRequest();
        leave.setEmployee(employee);
        leave.setStartDate(startDate);
        leave.setEndDate(endDate);
        leave.setType(type);

        return leaveRepository.save(leave);
    }

    // HR approves leave
    @PreAuthorize("hasRole('HR_MANAGER')")
    @PostMapping("/{id}/approve")
    public void approveLeave(@PathVariable Long id) {
        LeaveRequest leave = leaveRepository.findById(id).orElseThrow();
        leave.setStatus(LeaveStatus.APPROVED);
        leaveRepository.save(leave);
    }
}
