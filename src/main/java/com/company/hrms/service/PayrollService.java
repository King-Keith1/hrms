package com.company.hrms.service;

import com.company.hrms.entity.*;
import com.company.hrms.repository.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
public class PayrollService {

    private final AttendanceRepository attendanceRepository;
    private final LeaveRepository leaveRepository;
    private final PayrollRepository payrollRepository;

    public PayrollService(AttendanceRepository attendanceRepository,
                          LeaveRepository leaveRepository,
                          PayrollRepository payrollRepository) {
        this.attendanceRepository = attendanceRepository;
        this.leaveRepository = leaveRepository;
        this.payrollRepository = payrollRepository;
    }

    public Payroll generatePayroll(Employee employee, YearMonth month) {

        if (payrollRepository.existsByEmployeeIdAndMonth(employee.getId(), month)) {
            throw new RuntimeException("Payroll already generated");
        }

        LocalDate start = month.atDay(1);
        LocalDate end = month.atEndOfMonth();

        List<Attendance> attendanceList =
                attendanceRepository.findAll().stream()
                        .filter(a -> a.getEmployee().getId().equals(employee.getId()))
                        .filter(a -> !a.getDate().isBefore(start) && !a.getDate().isAfter(end))
                        .toList();

        BigDecimal total = BigDecimal.ZERO;

        for (Attendance a : attendanceList) {
            BigDecimal daily =
                    employee.getHourlyRate()
                            .multiply(BigDecimal.valueOf(a.getHoursWorked()))
                            .add(
                                    employee.getHourlyRate()
                                            .multiply(BigDecimal.valueOf(a.getOvertimeHours()))
                                            .multiply(BigDecimal.valueOf(1.5))
                            );
            total = total.add(daily);
        }

        // PAID LEAVE
        List<LeaveRequest> leaves =
                leaveRepository
                        .findByEmployeeIdAndStatusAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                                employee.getId(),
                                LeaveStatus.APPROVED,
                                end,
                                start
                        );

        for (LeaveRequest leave : leaves) {
            if (leave.getType() == LeaveType.PAID) {
                long days =
                        leave.getEndDate().toEpochDay()
                                - leave.getStartDate().toEpochDay() + 1;

                BigDecimal leavePay =
                        employee.getHourlyRate()
                                .multiply(BigDecimal.valueOf(
                                        employee.getStandardHoursPerDay() * days));

                total = total.add(leavePay);
            }
        }

        Payroll payroll = new Payroll();
        payroll.setEmployee(employee);
        payroll.setMonth(month);
        payroll.setGrossPay(total);

        return payrollRepository.save(payroll);
    }
}
