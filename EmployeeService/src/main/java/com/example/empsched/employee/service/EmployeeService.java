package com.example.empsched.employee.service;

import com.example.empsched.employee.entity.Employee;

import java.util.List;
import java.util.UUID;

public interface EmployeeService {
    List<Employee> getAllEmployees(final UUID organisationId);

    Employee createEmployee(final Employee employee, final UUID organisationId);

    void deleteEmployee(final UUID employeeId, final UUID organisationId);
}
