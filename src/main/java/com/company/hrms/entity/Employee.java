package com.company.hrms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(unique = true, nullable = false)
    private String employeeNumber;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "department_id")
    private Department department;

    @Column(nullable = false)
    private BigDecimal hourlyRate;

    @Column(nullable = false)
    private int standardHoursPerDay = 8;

    @Column(nullable = false)
    private boolean active = true;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    protected Employee() {}

    public Employee(String fullName,
                    String employeeNumber,
                    Department department,
                    BigDecimal hourlyRate) {
        this.fullName = fullName;
        this.employeeNumber = employeeNumber;
        this.department = department;
        this.hourlyRate = hourlyRate;
    }

    //GETTERS
    public Long getId() {
        return id;
    }
    public String getFullName() {
        return fullName;
    }
    public String getEmployeeNumber() {
        return employeeNumber;
    }
    public Department getDepartment() {
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

    //SETTERS
    public void setId(Long id) {
        this.id = id;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }
    public void setDepartment(Department department) {
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