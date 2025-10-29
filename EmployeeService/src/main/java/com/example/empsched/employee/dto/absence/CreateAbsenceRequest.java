package com.example.empsched.employee.dto.absence;

import com.example.empsched.employee.entity.AbsenceReason;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CreateAbsenceRequest(
        @Size(max = 3000)
        String description,

        @NotNull
        AbsenceReason reason,

        @NotNull
        LocalDate startDate,

        @NotNull
        LocalDate endDate
) {
}
