package com.company.hrms.controller;

import com.company.hrms.dto.CreateEmployeeRequest;
import com.company.hrms.entity.Employee;
import com.company.hrms.entity.User;
import com.company.hrms.repository.EmployeeRepository;
import com.company.hrms.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;

    public EmployeeController(EmployeeRepository employeeRepository,
                              UserRepository userRepository) {
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
    }

    @PreAuthorize("hasRole('HR_MANAGER') or hasRole('ADMIN')")
    @PostMapping
    public Employee createEmployee(@RequestBody CreateEmployeeRequest request) {

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Employee employee = new Employee();
        employee.setFullName(request.fullName());
        employee.setEmployeeNumber(request.employeeNumber());
        employee.setDepartment(request.department());
        employee.setHourlyRate(request.hourlyRate());
        employee.setUser(user);

        return employeeRepository.save(employee);
    }
}
