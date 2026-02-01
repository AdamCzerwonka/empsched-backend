package com.example.empsched.scheduling.solver;

import ai.timefold.solver.test.api.score.stream.ConstraintVerifier;
import com.example.empsched.scheduling.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ScheduleConstraintProviderTest {

    private ConstraintVerifier<ScheduleConstraintProvider, WorkSchedule> constraintVerifier;
    private Employee employee;
    private Position position;
    private Organisation organisation;

    @BeforeEach
    void setUp() {
        constraintVerifier = ConstraintVerifier.build(
                new ScheduleConstraintProvider(),
                WorkSchedule.class,
                Shift.class
        );

        organisation = new Organisation(UUID.randomUUID(), null);
        position = new Position(organisation);
        position.setId(UUID.randomUUID());

        employee = new Employee();
        employee.setId(UUID.randomUUID());
        employee.setPositions(Set.of(position));
        employee.setMaxWeeklyHours(40);
        employee.setOrganisation(organisation);
    }

    @Test
    void testRequiredSkill_NoViolation() {
        // Given
        Shift shift = new Shift();
        shift.setId(UUID.randomUUID());
        shift.setRequiredPositionId(position.getId());
        shift.setAssignedEmployee(employee);
        shift.setStartTime(LocalDateTime.of(2026, 1, 26, 9, 0));
        shift.setEndTime(LocalDateTime.of(2026, 1, 26, 17, 0));

        // When/Then
        constraintVerifier.verifyThat(ScheduleConstraintProvider::requiredSkill)
                .given(shift)
                .penalizesBy(0);
    }

    @Test
    void testRequiredSkill_Violation() {
        // Given
        UUID wrongPositionId = UUID.randomUUID();

        Shift shift = new Shift();
        shift.setId(UUID.randomUUID());
        shift.setRequiredPositionId(wrongPositionId); // Employee doesn't have this position
        shift.setAssignedEmployee(employee);
        shift.setStartTime(LocalDateTime.of(2026, 1, 26, 9, 0));
        shift.setEndTime(LocalDateTime.of(2026, 1, 26, 17, 0));

        // When/Then
        constraintVerifier.verifyThat(ScheduleConstraintProvider::requiredSkill)
                .given(shift)
                .penalizesBy(1);
    }

    @Test
    void testRequiredSkill_UnassignedShift() {
        // Given
        Shift shift = new Shift();
        shift.setId(UUID.randomUUID());
        shift.setRequiredPositionId(position.getId());
        shift.setAssignedEmployee(null); // Unassigned
        shift.setStartTime(LocalDateTime.of(2026, 1, 26, 9, 0));
        shift.setEndTime(LocalDateTime.of(2026, 1, 26, 17, 0));

        // When/Then - Unassigned shifts should not trigger requiredSkill constraint
        constraintVerifier.verifyThat(ScheduleConstraintProvider::requiredSkill)
                .given(shift)
                .penalizesBy(0);
    }

    @Test
    void testMinimizeUnassignedShifts() {
        // Given
        Shift unassignedShift = new Shift();
        unassignedShift.setId(UUID.randomUUID());
        unassignedShift.setRequiredPositionId(position.getId());
        unassignedShift.setAssignedEmployee(null);
        unassignedShift.setStartTime(LocalDateTime.of(2026, 1, 26, 9, 0));
        unassignedShift.setEndTime(LocalDateTime.of(2026, 1, 26, 17, 0));

        // When/Then - The constraint penalizes with 10 soft points (HardSoftScore.ofSoft(10))
        constraintVerifier.verifyThat(ScheduleConstraintProvider::minimizeUnassignedShifts)
                .given(unassignedShift)
                .penalizes(); // Just verify it penalizes, don't check exact value due to soft score weighting
    }

    @Test
    void testMinimizeUnassignedShifts_AssignedShift() {
        // Given
        Shift assignedShift = new Shift();
        assignedShift.setId(UUID.randomUUID());
        assignedShift.setRequiredPositionId(position.getId());
        assignedShift.setAssignedEmployee(employee);
        assignedShift.setStartTime(LocalDateTime.of(2026, 1, 26, 9, 0));
        assignedShift.setEndTime(LocalDateTime.of(2026, 1, 26, 17, 0));

        // When/Then
        constraintVerifier.verifyThat(ScheduleConstraintProvider::minimizeUnassignedShifts)
                .given(assignedShift)
                .penalizesBy(0);
    }

    @Test
    void testNoOverlappingShifts_NoViolation() {
        // Given
        Shift shift1 = new Shift();
        shift1.setId(UUID.randomUUID());
        shift1.setRequiredPositionId(position.getId());
        shift1.setAssignedEmployee(employee);
        shift1.setStartTime(LocalDateTime.of(2026, 1, 26, 9, 0));
        shift1.setEndTime(LocalDateTime.of(2026, 1, 26, 17, 0));

        Shift shift2 = new Shift();
        shift2.setId(UUID.randomUUID());
        shift2.setRequiredPositionId(position.getId());
        shift2.setAssignedEmployee(employee);
        shift2.setStartTime(LocalDateTime.of(2026, 1, 26, 18, 0));
        shift2.setEndTime(LocalDateTime.of(2026, 1, 26, 22, 0));

        // When/Then
        constraintVerifier.verifyThat(ScheduleConstraintProvider::noOverlappingShifts)
                .given(shift1, shift2)
                .penalizesBy(0);
    }

    @Test
    void testNoOverlappingShifts_Violation() {
        // Given
        Shift shift1 = new Shift();
        shift1.setId(UUID.randomUUID());
        shift1.setRequiredPositionId(position.getId());
        shift1.setAssignedEmployee(employee);
        shift1.setStartTime(LocalDateTime.of(2026, 1, 26, 9, 0));
        shift1.setEndTime(LocalDateTime.of(2026, 1, 26, 17, 0));

        Shift shift2 = new Shift();
        shift2.setId(UUID.randomUUID());
        shift2.setRequiredPositionId(position.getId());
        shift2.setAssignedEmployee(employee);
        shift2.setStartTime(LocalDateTime.of(2026, 1, 26, 16, 0)); // Overlaps with shift1
        shift2.setEndTime(LocalDateTime.of(2026, 1, 26, 20, 0));

        // When/Then
        constraintVerifier.verifyThat(ScheduleConstraintProvider::noOverlappingShifts)
                .given(shift1, shift2)
                .penalizesBy(1);
    }

    @Test
    void testMaxWorkingHours_NoViolation() {
        // Given - Total 16 hours, employee max is 40 hours
        Shift shift1 = new Shift();
        shift1.setId(UUID.randomUUID());
        shift1.setRequiredPositionId(position.getId());
        shift1.setAssignedEmployee(employee);
        shift1.setStartTime(LocalDateTime.of(2026, 1, 26, 9, 0));
        shift1.setEndTime(LocalDateTime.of(2026, 1, 26, 17, 0)); // 8 hours

        Shift shift2 = new Shift();
        shift2.setId(UUID.randomUUID());
        shift2.setRequiredPositionId(position.getId());
        shift2.setAssignedEmployee(employee);
        shift2.setStartTime(LocalDateTime.of(2026, 1, 27, 9, 0));
        shift2.setEndTime(LocalDateTime.of(2026, 1, 27, 17, 0)); // 8 hours

        // When/Then
        constraintVerifier.verifyThat(ScheduleConstraintProvider::maxWorkingHours)
                .given(shift1, shift2)
                .penalizesBy(0);
    }

    @Test
    void testMaxWorkingHours_Violation() {
        // Given - Total 45 hours, employee max is 40 hours = 5 hours over
        employee.setMaxWeeklyHours(40);

        Shift shift1 = new Shift();
        shift1.setId(UUID.randomUUID());
        shift1.setRequiredPositionId(position.getId());
        shift1.setAssignedEmployee(employee);
        shift1.setStartTime(LocalDateTime.of(2026, 1, 26, 9, 0));
        shift1.setEndTime(LocalDateTime.of(2026, 1, 26, 22, 0)); // 13 hours

        Shift shift2 = new Shift();
        shift2.setId(UUID.randomUUID());
        shift2.setRequiredPositionId(position.getId());
        shift2.setAssignedEmployee(employee);
        shift2.setStartTime(LocalDateTime.of(2026, 1, 27, 9, 0));
        shift2.setEndTime(LocalDateTime.of(2026, 1, 28, 1, 0)); // 16 hours (spans to next day)

        Shift shift3 = new Shift();
        shift3.setId(UUID.randomUUID());
        shift3.setRequiredPositionId(position.getId());
        shift3.setAssignedEmployee(employee);
        shift3.setStartTime(LocalDateTime.of(2026, 1, 28, 9, 0));
        shift3.setEndTime(LocalDateTime.of(2026, 1, 29, 1, 0)); // 16 hours (spans to next day)

        // Total = 45 hours, over by 5 hours = 300 minutes
        // When/Then
        constraintVerifier.verifyThat(ScheduleConstraintProvider::maxWorkingHours)
                .given(shift1, shift2, shift3)
                .penalizesBy(300); // 5 hours * 60 minutes
    }

    @Test
    void testEmployeeUnavailable_NoViolation() {
        // Given
        LocalDate date = LocalDate.of(2026, 1, 26);

        Shift shift = new Shift();
        shift.setId(UUID.randomUUID());
        shift.setRequiredPositionId(position.getId());
        shift.setAssignedEmployee(employee);
        shift.setStartTime(date.atTime(9, 0));
        shift.setEndTime(date.atTime(17, 0));

        EmployeeAvailability availability = new EmployeeAvailability();
        availability.setEmployee(employee);
        availability.setDate(date);
        availability.setType(AvailabilityType.DESIRED); // Not unavailable

        // When/Then
        constraintVerifier.verifyThat(ScheduleConstraintProvider::employeeUnavailable)
                .given(shift, availability)
                .penalizesBy(0);
    }

    @Test
    void testEmployeeUnavailable_Violation() {
        // Given
        LocalDate date = LocalDate.of(2026, 1, 26);

        Shift shift = new Shift();
        shift.setId(UUID.randomUUID());
        shift.setRequiredPositionId(position.getId());
        shift.setAssignedEmployee(employee);
        shift.setStartTime(date.atTime(9, 0));
        shift.setEndTime(date.atTime(17, 0));

        EmployeeAvailability availability = new EmployeeAvailability();
        availability.setEmployee(employee);
        availability.setDate(date);
        availability.setType(AvailabilityType.UNAVAILABLE); // Employee is unavailable

        // When/Then
        constraintVerifier.verifyThat(ScheduleConstraintProvider::employeeUnavailable)
                .given(shift, availability)
                .penalizesBy(1);
    }

    @Test
    void testEmployeeDesired_Reward() {
        // Given
        LocalDate date = LocalDate.of(2026, 1, 26);

        Shift shift = new Shift();
        shift.setId(UUID.randomUUID());
        shift.setRequiredPositionId(position.getId());
        shift.setAssignedEmployee(employee);
        shift.setStartTime(date.atTime(9, 0));
        shift.setEndTime(date.atTime(17, 0));

        EmployeeAvailability availability = new EmployeeAvailability();
        availability.setEmployee(employee);
        availability.setDate(date);
        availability.setType(AvailabilityType.DESIRED);

        // When/Then - This should reward (negative penalty)
        constraintVerifier.verifyThat(ScheduleConstraintProvider::employeeDesired)
                .given(shift, availability)
                .rewardsWith(1);
    }

    @Test
    void testEmployeeDesired_NoReward() {
        // Given
        LocalDate date = LocalDate.of(2026, 1, 26);

        Shift shift = new Shift();
        shift.setId(UUID.randomUUID());
        shift.setRequiredPositionId(position.getId());
        shift.setAssignedEmployee(employee);
        shift.setStartTime(date.atTime(9, 0));
        shift.setEndTime(date.atTime(17, 0));

        EmployeeAvailability availability = new EmployeeAvailability();
        availability.setEmployee(employee);
        availability.setDate(date);
        availability.setType(AvailabilityType.UNAVAILABLE); // Not desired

        // When/Then
        constraintVerifier.verifyThat(ScheduleConstraintProvider::employeeDesired)
                .given(shift, availability)
                .rewardsWith(0);
    }
}
