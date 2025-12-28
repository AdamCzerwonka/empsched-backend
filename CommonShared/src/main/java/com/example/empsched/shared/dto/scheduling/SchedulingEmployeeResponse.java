package com.example.empsched.shared.dto.scheduling;

import java.util.Set;
import java.util.UUID;

public record SchedulingEmployeeResponse(
        Set<UUID> positionIds,
        int maxWeeklyHours,
        UUID organisationId
) {
}
