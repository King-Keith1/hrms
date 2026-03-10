package com.company.hrms.service;

import com.company.hrms.entity.Employee;
import com.company.hrms.entity.LeaveRequest;
import com.company.hrms.entity.LeaveStatus;
import com.company.hrms.entity.LeaveType;
import com.company.hrms.repository.EmployeeRepository;
import com.company.hrms.repository.LeaveRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class LeaveService {

    private final LeaveRepository leaveRepository;
    private final EmployeeRepository employeeRepository;

    public LeaveService(LeaveRepository leaveRepository,
                        EmployeeRepository employeeRepository) {
        this.leaveRepository = leaveRepository;
        this.employeeRepository = employeeRepository;
    }

    public LeaveRequest requestLeave(
            String username,
            LocalDate startDate,
            LocalDate endDate,
            LeaveType type) {

        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }

        Employee employee = employeeRepository.findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        LeaveRequest leave = new LeaveRequest();
        leave.setEmployee(employee);
        leave.setStartDate(startDate);
        leave.setEndDate(endDate);
        leave.setType(type);
        leave.setStatus(LeaveStatus.PENDING);

        return leaveRepository.save(leave);
    }

    public void approveLeave(Long id) {

        LeaveRequest leave = leaveRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave not found"));

        leave.setStatus(LeaveStatus.APPROVED);

        leaveRepository.save(leave);
    }
}