package com.example.empsched.scheduling.dto;

import java.util.UUID;

public record ShiftRequirement(
        UUID positionId,
        int quantity
) {
}
