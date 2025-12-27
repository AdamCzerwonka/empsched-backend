package com.example.empsched.scheduling.dto;

import com.example.empsched.scheduling.entity.ScheduleStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record ScheduleDTO(
        UUID id,
        LocalDate startDate,
        LocalDate endDate,
        ScheduleStatus status,
        String score,
        List<ShiftDTO> shiftList
) {
}
