package com.example.empsched.scheduling.dto;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public record ScheduleGenerationRequestDTO(
        LocalDate startDate,
        LocalDate endDate,
        Map<DayOfWeek, List<ShiftDefinitionDTO>> weeklyPattern,
        Map<LocalDate, List<ShiftDefinitionDTO>> dateOverrides
) {
}
