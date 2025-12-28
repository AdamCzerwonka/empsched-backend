package com.example.empsched.scheduling.dto;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public record ScheduleGenerationRequest(
        LocalDate startDate,
        LocalDate endDate,
        Map<DayOfWeek, List<ShiftDefinition>> weeklyPattern,
        Map<LocalDate, List<ShiftDefinition>> dateOverrides
) {
}
