package com.company.hrms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;

    @Column(unique = true)
    private String employeeNumber;

    private String department;

    private BigDecimal hourlyRate;

    private int standardHoursPerDay = 8;

    private boolean active = true;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    // ===== GETTERS =====
    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public String getDepartment() {
        return department;
    }

    public BigDecimal getHourlyRate() {
        return hourlyRate;
    }

    public int getStandardHoursPerDay() {
        return standardHoursPerDay;
    }

    public boolean isActive() {
        return active;
    }

    public User getUser() {
        return user;
    }

    // ===== SETTERS =====
    public void setId(Long id) {
        this.id = id;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setHourlyRate(BigDecimal hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public void setStandardHoursPerDay(int standardHoursPerDay) {
        this.standardHoursPerDay = standardHoursPerDay;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
