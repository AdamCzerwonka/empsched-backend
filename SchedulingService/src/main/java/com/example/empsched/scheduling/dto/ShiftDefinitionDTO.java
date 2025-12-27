package com.example.empsched.scheduling.dto;

import java.time.LocalTime;
import java.util.List;

public record ShiftDefinitionDTO(
        LocalTime startTime,
        LocalTime endTime,
        List<ShiftRequirementDTO> shiftRequirements
) {
}
