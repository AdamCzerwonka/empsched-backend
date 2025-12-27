package com.example.empsched.scheduling.dto;

import com.example.empsched.scheduling.entity.Employee;

import java.time.LocalDateTime;
import java.util.UUID;

public record ShiftDTO(
        UUID id,
        LocalDateTime startTime,
        LocalDateTime endTime,
        UUID requiredPositionId,
        Employee assignedEmployee
) {
}
