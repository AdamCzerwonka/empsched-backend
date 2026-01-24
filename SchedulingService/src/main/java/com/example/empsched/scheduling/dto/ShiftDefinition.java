package com.example.empsched.scheduling.dto;

import java.time.LocalTime;
import java.util.List;

public record ShiftDefinition(
        LocalTime startTime,
        LocalTime endTime,
        List<ShiftRequirement> shiftRequirements
) {
}
