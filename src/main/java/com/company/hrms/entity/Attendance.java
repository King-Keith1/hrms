package com.company.hrms.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(
        name = "attendance",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"employee_id", "date"})
        }
)
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private int hoursWorked;

    private int overtimeHours;

    protected Attendance() {}

    public Attendance(Employee employee, LocalDate date, int hoursWorked, int overtimeHours) {
        this.employee = employee;
        this.date = date;
        this.hoursWorked = hoursWorked;
        this.overtimeHours = overtimeHours;
    }

    //GETTERS
    public Long getId() {
        return id;
    }
    public Employee getEmployee() {
        return employee;
    }
    public LocalDate getDate() {
        return date;
    }
    public int getHoursWorked() {
        return hoursWorked;
    }
    public int getOvertimeHours() {
        return overtimeHours;
    }

    //SETTERS
    public void setId(Long id) {
        this.id = id;
    }
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
    public void setHoursWorked(int hoursWorked) {
        this.hoursWorked = hoursWorked;
    }
    public void setOvertimeHours(int overtimeHours) {
        this.overtimeHours = overtimeHours;
    }
}
