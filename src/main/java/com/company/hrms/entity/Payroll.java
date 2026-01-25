package com.company.hrms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.YearMonth;

@Entity
public class Payroll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Employee employee;

    private YearMonth month;

    private BigDecimal grossPay;

    // GETTERS / SETTERS
    public Long getId() { return id; }
    public Employee getEmployee() { return employee; }
    public YearMonth getMonth() { return month; }
    public BigDecimal getGrossPay() { return grossPay; }

    public void setEmployee(Employee employee) { this.employee = employee; }
    public void setMonth(YearMonth month) { this.month = month; }
    public void setGrossPay(BigDecimal grossPay) { this.grossPay = grossPay; }
}
