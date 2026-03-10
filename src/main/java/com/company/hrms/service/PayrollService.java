package com.company.hrms.service;

import com.company.hrms.dto.PayslipResponse;
import com.company.hrms.entity.*;
import com.company.hrms.repository.*;
import org.springframework.stereotype.Service;
import com.company.hrms.repository.PayrollRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
public class PayrollService {

    private final AttendanceRepository attendanceRepository;
    private final LeaveRepository leaveRepository;
    private final PayrollRepository payrollRepository;
    private final EmployeeRepository employeeRepository;

    public PayrollService(AttendanceRepository attendanceRepository,
                          LeaveRepository leaveRepository,
                          PayrollRepository payrollRepository,
                          EmployeeRepository employeeRepository) {

        this.attendanceRepository = attendanceRepository;
        this.leaveRepository = leaveRepository;
        this.payrollRepository = payrollRepository;
        this.employeeRepository = employeeRepository;
    }

    // PUBLIC API METHOD
    public PayslipResponse generatePayroll(Long employeeId, YearMonth month) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        Payroll payroll = generatePayrollInternal(employee, month);

        return new PayslipResponse(
                employee.getFullName(),
                month,
                payroll.getGrossPay()
        );
    }

    // INTERNAL BUSINESS LOGIC
    private Payroll generatePayrollInternal(Employee employee, YearMonth month) {

        String payrollMonth = month.toString();

        if (payrollRepository.existsByEmployee_IdAndPayrollMonth(
                employee.getId(), payrollMonth)) {
            throw new RuntimeException("Payroll already generated");
        }

        LocalDate start = month.atDay(1);
        LocalDate end = month.atEndOfMonth();

        List<Attendance> attendanceList =
                attendanceRepository.findByEmployee_IdAndDateBetween(
                        employee.getId(),
                        start,
                        end
                );

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
                        .findByEmployee_IdAndStatusAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
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

        Payroll payroll = new Payroll(employee, payrollMonth, total);
        return payrollRepository.save(payroll);
    }
}