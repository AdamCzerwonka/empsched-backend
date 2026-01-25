package com.example.empsched.scheduling.service;

import com.example.empsched.scheduling.dto.ScheduleGenerationRequest;
import com.example.empsched.scheduling.dto.ShiftDefinition;
import com.example.empsched.scheduling.dto.ShiftRequirement;
import com.example.empsched.scheduling.entity.Schedule;
import com.example.empsched.scheduling.entity.Shift;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ShiftGeneratorServiceTest {

    private ShiftGeneratorService shiftGeneratorService;
    private Schedule schedule;

    @BeforeEach
    void setUp() {
        shiftGeneratorService = new ShiftGeneratorService();
        schedule = new Schedule();
    }

    @Test
    void testGenerateShiftsFromRequest_WithWeeklyPattern() {
        // Given
        UUID positionId = UUID.randomUUID();
        LocalDate startDate = LocalDate.of(2026, 1, 26); // Monday
        LocalDate endDate = LocalDate.of(2026, 2, 1); // Sunday (7 days)

        ShiftRequirement requirement = new ShiftRequirement(positionId, 2);
        ShiftDefinition morningShift = new ShiftDefinition(
                LocalTime.of(9, 0),
                LocalTime.of(17, 0),
                List.of(requirement)
        );

        Map<DayOfWeek, List<ShiftDefinition>> weeklyPattern = new HashMap<>();
        weeklyPattern.put(DayOfWeek.MONDAY, List.of(morningShift));
        weeklyPattern.put(DayOfWeek.WEDNESDAY, List.of(morningShift));
        weeklyPattern.put(DayOfWeek.FRIDAY, List.of(morningShift));

        ScheduleGenerationRequest request = new ScheduleGenerationRequest(
                startDate,
                endDate,
                weeklyPattern,
                null
        );

        // When
        List<Shift> shifts = shiftGeneratorService.generateShiftsFromRequest(schedule, request);

        // Then
        assertNotNull(shifts);
        assertEquals(6, shifts.size()); // 3 days * 2 shifts per day

        // Verify all shifts are assigned to the schedule
        shifts.forEach(shift -> assertEquals(schedule, shift.getSchedule()));

        // Verify shift times are correct
        long mondayShifts = shifts.stream()
                .filter(s -> s.getStartTime().getDayOfWeek() == DayOfWeek.MONDAY)
                .count();
        assertEquals(2, mondayShifts);
    }

    @Test
    void testGenerateShiftsFromRequest_WithDateOverrides() {
        // Given
        UUID positionId = UUID.randomUUID();
        LocalDate startDate = LocalDate.of(2026, 1, 26);
        LocalDate endDate = LocalDate.of(2026, 1, 28);
        LocalDate overrideDate = LocalDate.of(2026, 1, 27);

        ShiftRequirement requirement = new ShiftRequirement(positionId, 1);
        ShiftDefinition normalShift = new ShiftDefinition(
                LocalTime.of(9, 0),
                LocalTime.of(17, 0),
                List.of(requirement)
        );
        ShiftDefinition overrideShift = new ShiftDefinition(
                LocalTime.of(10, 0),
                LocalTime.of(14, 0),
                List.of(requirement)
        );

        Map<DayOfWeek, List<ShiftDefinition>> weeklyPattern = new HashMap<>();
        weeklyPattern.put(DayOfWeek.MONDAY, List.of(normalShift));
        weeklyPattern.put(DayOfWeek.TUESDAY, List.of(normalShift));

        Map<LocalDate, List<ShiftDefinition>> dateOverrides = new HashMap<>();
        dateOverrides.put(overrideDate, List.of(overrideShift));

        ScheduleGenerationRequest request = new ScheduleGenerationRequest(
                startDate,
                endDate,
                weeklyPattern,
                dateOverrides
        );

        // When
        List<Shift> shifts = shiftGeneratorService.generateShiftsFromRequest(schedule, request);

        // Then
        assertNotNull(shifts);
        assertEquals(2, shifts.size()); // 1 shift on Monday, 1 override shift on Tuesday

        // Verify override is applied
        Shift overriddenShift = shifts.stream()
                .filter(s -> s.getStartTime().toLocalDate().equals(overrideDate))
                .findFirst()
                .orElseThrow();
        assertEquals(LocalTime.of(10, 0), overriddenShift.getStartTime().toLocalTime());
        assertEquals(LocalTime.of(14, 0), overriddenShift.getEndTime().toLocalTime());
    }

    @Test
    void testGenerateShiftsFromRequest_NightShiftSpansMidnight() {
        // Given
        UUID positionId = UUID.randomUUID();
        LocalDate startDate = LocalDate.of(2026, 1, 26);
        LocalDate endDate = LocalDate.of(2026, 1, 26);

        ShiftRequirement requirement = new ShiftRequirement(positionId, 1);
        ShiftDefinition nightShift = new ShiftDefinition(
                LocalTime.of(22, 0),
                LocalTime.of(6, 0), // Next day
                List.of(requirement)
        );

        Map<DayOfWeek, List<ShiftDefinition>> weeklyPattern = new HashMap<>();
        weeklyPattern.put(DayOfWeek.MONDAY, List.of(nightShift));

        ScheduleGenerationRequest request = new ScheduleGenerationRequest(
                startDate,
                endDate,
                weeklyPattern,
                null
        );

        // When
        List<Shift> shifts = shiftGeneratorService.generateShiftsFromRequest(schedule, request);

        // Then
        assertNotNull(shifts);
        assertEquals(1, shifts.size());

        Shift shift = shifts.get(0);
        assertEquals(LocalDateTime.of(2026, 1, 26, 22, 0), shift.getStartTime());
        assertEquals(LocalDateTime.of(2026, 1, 27, 6, 0), shift.getEndTime());
    }

    @Test
    void testGenerateShiftsFromRequest_MultiplePositions() {
        // Given
        UUID position1 = UUID.randomUUID();
        UUID position2 = UUID.randomUUID();
        LocalDate startDate = LocalDate.of(2026, 1, 26);
        LocalDate endDate = LocalDate.of(2026, 1, 26);

        ShiftRequirement requirement1 = new ShiftRequirement(position1, 2);
        ShiftRequirement requirement2 = new ShiftRequirement(position2, 1);

        ShiftDefinition shiftDef = new ShiftDefinition(
                LocalTime.of(9, 0),
                LocalTime.of(17, 0),
                List.of(requirement1, requirement2)
        );

        Map<DayOfWeek, List<ShiftDefinition>> weeklyPattern = new HashMap<>();
        weeklyPattern.put(DayOfWeek.MONDAY, List.of(shiftDef));

        ScheduleGenerationRequest request = new ScheduleGenerationRequest(
                startDate,
                endDate,
                weeklyPattern,
                null
        );

        // When
        List<Shift> shifts = shiftGeneratorService.generateShiftsFromRequest(schedule, request);

        // Then
        assertNotNull(shifts);
        assertEquals(3, shifts.size()); // 2 for position1 + 1 for position2

        long position1Count = shifts.stream()
                .filter(s -> s.getRequiredPositionId().equals(position1))
                .count();
        long position2Count = shifts.stream()
                .filter(s -> s.getRequiredPositionId().equals(position2))
                .count();

        assertEquals(2, position1Count);
        assertEquals(1, position2Count);
    }

    @Test
    void testGenerateShiftsFromRequest_EmptyWeeklyPattern() {
        // Given
        LocalDate startDate = LocalDate.of(2026, 1, 26);
        LocalDate endDate = LocalDate.of(2026, 1, 28);

        ScheduleGenerationRequest request = new ScheduleGenerationRequest(
                startDate,
                endDate,
                new HashMap<>(),
                null
        );

        // When
        List<Shift> shifts = shiftGeneratorService.generateShiftsFromRequest(schedule, request);

        // Then
        assertNotNull(shifts);
        assertTrue(shifts.isEmpty());
    }

    @Test
    void testGenerateShiftsFromRequest_NullShiftRequirements() {
        // Given
        LocalDate startDate = LocalDate.of(2026, 1, 26);
        LocalDate endDate = LocalDate.of(2026, 1, 26);

        ShiftDefinition shiftDef = new ShiftDefinition(
                LocalTime.of(9, 0),
                LocalTime.of(17, 0),
                null // No requirements
        );

        Map<DayOfWeek, List<ShiftDefinition>> weeklyPattern = new HashMap<>();
        weeklyPattern.put(DayOfWeek.MONDAY, List.of(shiftDef));

        ScheduleGenerationRequest request = new ScheduleGenerationRequest(
                startDate,
                endDate,
                weeklyPattern,
                null
        );

        // When
        List<Shift> shifts = shiftGeneratorService.generateShiftsFromRequest(schedule, request);

        // Then
        assertNotNull(shifts);
        assertTrue(shifts.isEmpty());
    }
}
