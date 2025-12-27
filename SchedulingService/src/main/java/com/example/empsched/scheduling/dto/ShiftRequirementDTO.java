package com.example.empsched.scheduling.dto;

import java.util.UUID;

public record ShiftRequirementDTO(
        UUID positionId,
        int quantity
) {
}
