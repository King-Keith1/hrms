package com.company.hrms.repository;

import com.company.hrms.entity.Payroll;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.YearMonth;

public interface PayrollRepository extends JpaRepository<Payroll, Long> {

    boolean existsByEmployeeIdAndMonth(Long employeeId, YearMonth month);
}
