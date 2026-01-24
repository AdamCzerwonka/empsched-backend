package com.example.empsched.employee.dto.absence;

import com.example.empsched.shared.dto.employee.EmployeeResponse;

import java.time.LocalDate;
import java.util.UUID;

public record AbsenceResponse(
        UUID id,
        String description,
        String reason,
        EmployeeResponse employee,
        boolean approved,
        LocalDate startDate,
        LocalDate endDate
) {
}
