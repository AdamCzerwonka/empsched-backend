package com.example.empsched.employee.dto.absence;

import java.time.LocalDate;
import java.util.UUID;

public record AbsenceResponse(
        UUID id,
        String description,
        String reason,
        UUID employeeId,
        boolean approved,
        LocalDate startDate,
        LocalDate endDate
) {
}
