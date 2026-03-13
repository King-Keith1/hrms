package com.company.hrms.service;

import com.company.hrms.dto.CreateEmployeeRequest;
import com.company.hrms.entity.Department;
import com.company.hrms.entity.Employee;
import com.company.hrms.entity.Role;
import com.company.hrms.entity.User;
import com.company.hrms.repository.DepartmentRepository;
import com.company.hrms.repository.EmployeeRepository;
import com.company.hrms.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;

    public EmployeeService(EmployeeRepository employeeRepository,
                           UserRepository userRepository,
                           DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
    }

    public Employee createEmployee(CreateEmployeeRequest request, String username) {

        User creator = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Department department = departmentRepository.findById(request.departmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        if (creator.getRole() != Role.ROLE_MANAGER) {
            if (!creator.getDepartment().getId().equals(department.getId())) {
                throw new RuntimeException("Cannot create employees outside your department");
            }
        }

        Employee employee = new Employee(
                request.fullName(),
                request.employeeNumber(),
                department,
                request.hourlyRate()
        );

        return employeeRepository.save(employee);
    }
}