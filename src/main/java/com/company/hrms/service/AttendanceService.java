package com.company.hrms.service;

import com.company.hrms.entity.Attendance;
import com.company.hrms.entity.Employee;
import com.company.hrms.repository.AttendanceRepository;
import com.company.hrms.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import com.company.hrms.entity.Attendance;

import java.time.LocalDate;

@Service
public class AttendanceService {

    private final EmployeeRepository employeeRepository;
    private final AttendanceRepository attendanceRepository;

    public AttendanceService(EmployeeRepository employeeRepository,
                             AttendanceRepository attendanceRepository) {
        this.employeeRepository = employeeRepository;
        this.attendanceRepository = attendanceRepository;
    }

    public void markAttendance(String username, int hoursWorked) {

        Employee employee = employeeRepository.findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        LocalDate today = LocalDate.now();

        attendanceRepository.findByEmployee_IdAndDate(employee.getId(), today)
                .ifPresent(a -> {
                    throw new RuntimeException("Attendance already marked for today");
                });

        int overtime = Math.max(0, hoursWorked - employee.getStandardHoursPerDay());

        Attendance attendance = new Attendance();
        attendance.setEmployee(employee);
        attendance.setHoursWorked(hoursWorked);
        attendance.setOvertimeHours(overtime);
        attendance.setDate(today);

        attendanceRepository.save(attendance);
    }
}