package com.example.empsched.employee.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

public interface EmployeeService {
    void createEmployee(String email, String password);

}
