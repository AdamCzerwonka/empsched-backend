package com.example.empsched.shared.dto.employee;

import com.example.empsched.shared.dto.position.PositionResponse;

import java.util.List;
import java.util.UUID;

public record EmployeeResponse(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        UUID organisationId,
        List<PositionResponse> positions
) {
}
