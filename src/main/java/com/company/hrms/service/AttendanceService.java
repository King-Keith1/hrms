package com.company.hrms.service;

import com.company.hrms.entity.Attendance;
import com.company.hrms.entity.Employee;
import com.company.hrms.repository.AttendanceRepository;
import com.company.hrms.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * Service layer for managing employee attendance.
 * Handles business logic for marking attendance and calculating overtime.
 */
@Service
public class AttendanceService {

    private final EmployeeRepository employeeRepository;
    private final AttendanceRepository attendanceRepository;
    private final SystemClockService clockService;

    public AttendanceService(EmployeeRepository employeeRepository,
                             AttendanceRepository attendanceRepository,
                             SystemClockService clockService) {
        this.employeeRepository = employeeRepository;
        this.attendanceRepository = attendanceRepository;
        this.clockService = clockService;
    }

    /**
     * Marks attendance for the employee identified by username.
     * Public method invoked by controller.
     *
     * @param username    Employee's username
     * @param hoursWorked Number of hours worked today
     */
    public void markAttendance(String username, int hoursWorked) {

        // Retrieve employee by linked User entity
        Employee employee = employeeRepository.findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        markAttendanceInternal(employee, hoursWorked);
    }

    /**
     * Internal business logic for recording attendance.
     * Calculates overtime and prevents double-marking.
     *
     * @param employee    Employee entity
     * @param hoursWorked Hours worked
     */
    private void markAttendanceInternal(Employee employee, int hoursWorked) {

        // Validate input
        if (hoursWorked <= 0) {
            throw new IllegalArgumentException("Hours worked must be greater than 0");
        }

        LocalDate today = clockService.today(); // Get current system date

        // Prevent duplicate attendance for the same day
        attendanceRepository.findByEmployee_IdAndDate(employee.getId(), today)
                .ifPresent(a -> {
                    throw new RuntimeException("Attendance already marked for today");
                });

        // Calculate overtime
        int overtime = Math.max(0, hoursWorked - employee.getStandardHoursPerDay());

        // Create and save attendance record
        Attendance attendance = new Attendance(
                employee,
                today,
                hoursWorked,
                overtime
        );

        attendanceRepository.save(attendance);
    }
}