package com.example.empsched.employee.service;

import com.example.empsched.employee.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface EmployeeService {
    Page<Employee> getAllEmployees(final UUID organisationId, final Pageable pageable);

    Employee createEmployee(final Employee employee, final UUID organisationId);

    void deleteEmployee(final UUID employeeId, final UUID organisationId);
}
