package com.example.empsched.scheduling.service;

import com.example.empsched.scheduling.dto.ScheduleGenerationRequest;
import com.example.empsched.scheduling.dto.ShiftDefinition;
import com.example.empsched.scheduling.dto.ShiftRequirement;
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

    public List<Shift> generateShiftsFromRequest(Schedule schedule, ScheduleGenerationRequest request) {
        List<Shift> newShifts = new ArrayList<>();
        LocalDate currentDate = request.startDate();
        LocalDate endDate = request.endDate();

        while (!currentDate.isAfter(endDate)) {
            List<ShiftDefinition> dayDefinitions = getDefinitionsForDate(currentDate, request);

            if (dayDefinitions != null && !dayDefinitions.isEmpty()) {
                for (ShiftDefinition def : dayDefinitions) {
                    newShifts.addAll(createShiftsFromDefinition(schedule, currentDate, def));
                }
            }

            currentDate = currentDate.plusDays(1);
        }
        return newShifts;
    }

    private List<ShiftDefinition> getDefinitionsForDate(LocalDate date, ScheduleGenerationRequest request) {
        if (request.dateOverrides() != null && request.dateOverrides().containsKey(date)) {
            return request.dateOverrides().get(date);
        }

        if (request.weeklyPattern() != null && request.weeklyPattern().containsKey(date.getDayOfWeek())) {
            return request.weeklyPattern().get(date.getDayOfWeek());
        }

        return Collections.emptyList();
    }

    private List<Shift> createShiftsFromDefinition(Schedule schedule, LocalDate date, ShiftDefinition def) {
        List<Shift> shifts = new ArrayList<>();

        if (def.shiftRequirements() == null) return shifts;

        for (ShiftRequirement req : def.shiftRequirements()) {
            for (int i = 0; i < req.quantity(); i++) {
                Shift shift = new Shift();
                shift.setSchedule(schedule);
                shift.setRequiredPositionId(req.positionId());

                shift.setStartTime(LocalDateTime.of(date, def.startTime()));

                if (def.endTime().isBefore(def.startTime())) {
                    shift.setEndTime(LocalDateTime.of(date.plusDays(1), def.endTime()));
                } else {
                    shift.setEndTime(LocalDateTime.of(date, def.endTime()));
                }

                shift.setAssignedEmployee(null);

                shifts.add(shift);
            }
        }
        return shifts;
    }


}

