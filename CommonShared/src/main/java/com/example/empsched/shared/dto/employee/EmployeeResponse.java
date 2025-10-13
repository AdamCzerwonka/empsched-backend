package com.example.empsched.shared.dto.employee;

import java.util.UUID;

public record EmployeeResponse(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        UUID organisationId
) {
}
