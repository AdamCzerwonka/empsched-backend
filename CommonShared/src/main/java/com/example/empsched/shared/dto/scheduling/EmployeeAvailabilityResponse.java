package com.example.empsched.shared.dto.scheduling;

public record EmployeeAvailabilityResponse(
        String id,
        String employeeId,
        String date,
        String type
) {
}
