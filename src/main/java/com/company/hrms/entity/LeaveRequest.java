package com.company.hrms.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "leave_requests")
public class LeaveRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LeaveType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LeaveStatus status = LeaveStatus.PENDING;

    protected LeaveRequest() {}

    public LeaveRequest(Employee employee,
                        LocalDate startDate,
                        LocalDate endDate,
                        LeaveType type) {
        this.employee = employee;
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
    }

    //GETTERS
    public Long getId() { return id; }
    public Employee getEmployee() { return employee; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public LeaveType getType() { return type; }
    public LeaveStatus getStatus() { return status; }

    //SETTERS
    public void setEmployee(Employee employee) { this.employee = employee; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public void setType(LeaveType type) { this.type = type; }
    public void setStatus(LeaveStatus status) { this.status = status; }
}
