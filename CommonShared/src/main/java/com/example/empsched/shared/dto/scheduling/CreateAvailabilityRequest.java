package com.example.empsched.shared.dto.scheduling;

import java.time.LocalDate;
import java.util.UUID;

public record CreateAvailabilityRequest(
        LocalDate startDate,
        LocalDate endDate,
        boolean isAvailable,
        UUID employeeId,
        UUID absenceId
) {
}
