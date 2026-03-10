package com.company.hrms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.YearMonth;

@Entity
@Table(name = "payroll")
public class Payroll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal grossPay;

    @Column(name = "payroll_month", nullable = false)
    private String payrollMonth;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    protected Payroll() {}

    public Payroll(Employee employee, String payrollMonth, BigDecimal grossPay) {
        this.employee = employee;
        this.payrollMonth = payrollMonth;
        this.grossPay = grossPay;
    }

    //GETTERS
    public Long getId() {
        return id;
    }
    public BigDecimal getGrossPay() {
        return grossPay;
    }
    public String getPayrollMonth() {
        return payrollMonth;
    }
    public Employee getEmployee() {
        return employee;
    }

    //SETTERS
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
    public void setPayrollMonth(String payrollMonth) {
        this.payrollMonth = payrollMonth;
    }
    public void setGrossPay(BigDecimal grossPay) {
        this.grossPay = grossPay;
    }
}
