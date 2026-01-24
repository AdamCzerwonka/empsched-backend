package com.example.empsched.scheduling.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ShiftUpdateDTO(
        LocalDateTime startTime,
        LocalDateTime endTime,
        UUID requiredPositionId,
        UUID assignedEmployeeId
) {
}
