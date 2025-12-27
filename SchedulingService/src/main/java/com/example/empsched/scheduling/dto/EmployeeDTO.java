package com.example.empsched.scheduling.dto;

import java.util.Set;
import java.util.UUID;

public record EmployeeDTO(
        Set<UUID> positionIds,
        int maxWeeklyHours,
        UUID organisationId
) {
}
