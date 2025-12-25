package com.example.empsched.schedulingservice.dto;

import com.example.empsched.schedulingservice.entity.ContractType;

import java.util.Set;

public record SchedulingEmployeeDTO(
        Set<String> skills,
        ContractType contractType,
        int maxWeeklyHours
) {
}
