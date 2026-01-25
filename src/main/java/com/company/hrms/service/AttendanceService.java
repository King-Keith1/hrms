package com.company.hrms.service;

import com.company.hrms.entity.Attendance;
import com.company.hrms.entity.Employee;
import com.company.hrms.repository.AttendanceRepository;
import org.springframework.stereotype.Service;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final SystemClockService clockService;

    public AttendanceService(AttendanceRepository attendanceRepository,
                             SystemClockService clockService) {
        this.attendanceRepository = attendanceRepository;
        this.clockService = clockService;
    }

    public Attendance markAttendance(Employee employee, int hoursWorked) {

        int standard = employee.getStandardHoursPerDay();
        int overtime = Math.max(0, hoursWorked - standard);

        Attendance attendance = new Attendance();
        attendance.setEmployee(employee);
        attendance.setDate(clockService.today());
        attendance.setHoursWorked(hoursWorked);
        attendance.setOvertimeHours(overtime);

        return attendanceRepository.save(attendance);
    }
}
