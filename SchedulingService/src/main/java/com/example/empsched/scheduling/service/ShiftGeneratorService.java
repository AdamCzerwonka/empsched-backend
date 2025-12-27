package com.example.empsched.scheduling.service;

import com.example.empsched.scheduling.dto.ScheduleGenerationRequestDTO;
import com.example.empsched.scheduling.dto.ShiftDefinitionDTO;
import com.example.empsched.scheduling.dto.ShiftRequirementDTO;
import com.example.empsched.scheduling.entity.Schedule;
import com.example.empsched.scheduling.entity.Shift;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ShiftGeneratorService {

    public List<Shift> generateShiftsFromRequest(Schedule schedule, ScheduleGenerationRequestDTO request) {
        List<Shift> newShifts = new ArrayList<>();
        LocalDate currentDate = request.startDate();
        LocalDate endDate = request.endDate();

        while (!currentDate.isAfter(endDate)) {
            // 1. Determine which definitions apply for today
            List<ShiftDefinitionDTO> dayDefinitions = getDefinitionsForDate(currentDate, request);

            // 2. Generate actual entities based on definitions
            if (dayDefinitions != null && !dayDefinitions.isEmpty()) {
                for (ShiftDefinitionDTO def : dayDefinitions) {
                    newShifts.addAll(createShiftsFromDefinition(schedule, currentDate, def));
                }
            }

            currentDate = currentDate.plusDays(1);
        }
        return newShifts;
    }

    private List<ShiftDefinitionDTO> getDefinitionsForDate(LocalDate date, ScheduleGenerationRequestDTO request) {
        // Priority 1: Specific Date Override
        if (request.dateOverrides() != null && request.dateOverrides().containsKey(date)) {
            return request.dateOverrides().get(date);
        }

        // Priority 2: Weekly Pattern
        if (request.weeklyPattern() != null && request.weeklyPattern().containsKey(date.getDayOfWeek())) {
            return request.weeklyPattern().get(date.getDayOfWeek());
        }

        // Priority 3: No shifts defined for this day (e.g., weekend if not configured)
        return Collections.emptyList();
    }

    private List<Shift> createShiftsFromDefinition(Schedule schedule, LocalDate date, ShiftDefinitionDTO def) {
        List<Shift> shifts = new ArrayList<>();

        if (def.shiftRequirements() == null) return shifts;

        for (ShiftRequirementDTO req : def.shiftRequirements()) {
            // Create 'N' copies of the shift for 'N' employees required
            for (int i = 0; i < req.quantity(); i++) {
                Shift shift = new Shift();
                shift.setSchedule(schedule);
                shift.setRequiredPositionId(req.positionId());

                // Handle Start/End LocalDateTime
                shift.setStartTime(LocalDateTime.of(date, def.startTime()));

                // Handle overnight shifts (end time is before start time)
                if (def.endTime().isBefore(def.startTime())) {
                    shift.setEndTime(LocalDateTime.of(date.plusDays(1), def.endTime()));
                } else {
                    shift.setEndTime(LocalDateTime.of(date, def.endTime()));
                }

                // Explicitly set null to indicate unassigned
                shift.setAssignedEmployee(null);

                shifts.add(shift);
            }
        }
        return shifts;
    }


}

