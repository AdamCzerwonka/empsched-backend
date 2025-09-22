package com.example.empsched.employee.controller;

import com.example.empsched.employee.dto.CreateEmployeeRequest;
import com.example.empsched.employee.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<Void> createEmployee(@RequestBody @Valid CreateEmployeeRequest request) {

        employeeService.createEmployee(request.email(), request.password());
        // Logic to create an employee
        return ResponseEntity.ok().build();
    }
}
