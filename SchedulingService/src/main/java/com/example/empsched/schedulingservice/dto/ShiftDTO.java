package com.example.empsched.schedulingservice.dto;

import com.example.empsched.schedulingservice.entity.SchedulingEmployee;

import java.time.LocalDateTime;
import java.util.UUID;

public record ShiftDTO(
        UUID id,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String requiredSkill,
        SchedulingEmployee assignedEmployee
) {
}
