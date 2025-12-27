package com.example.empsched.scheduling.dto;

import java.time.LocalDate;
import java.util.UUID;

public record CreateAvailabilityDTO(
        LocalDate startDate,
        LocalDate endDate,
        boolean isAvailable,
        UUID employeeId
) {
}
