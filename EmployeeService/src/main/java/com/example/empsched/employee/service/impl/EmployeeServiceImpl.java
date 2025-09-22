package com.example.empsched.employee.service.impl;

import com.example.empsched.employee.service.EmployeeService;
import com.example.empsched.shared.dto.user.CreateUserRequest;
import com.example.empsched.shared.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    @Override
    public void createEmployee(String email, String password) {
        UUID uuid = UUID.randomUUID();
        CreateUserRequest createUserRequest = CreateUserRequest.builder()
                .id(uuid)
                .email(email)
                .password(password)
                .role(Role.ORGANISATION_EMPLOYEE)
                .build();
    }
}
