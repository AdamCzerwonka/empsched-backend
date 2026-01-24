package com.example.empsched.scheduling.solver;

import ai.timefold.solver.core.api.score.buildin.hardsoft.HardSoftScore;
import ai.timefold.solver.core.api.score.stream.*;
import com.example.empsched.scheduling.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@Slf4j
public class ScheduleConstraintProvider implements ConstraintProvider {

    @Override
    public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[] {
                requiredSkill(constraintFactory),
                maxWorkingHours(constraintFactory),
                noOverlappingShifts(constraintFactory),
                employeeUnavailable(constraintFactory),
                employeeDesired(constraintFactory),
                minimizeUnassignedShifts(constraintFactory)

        };
    }

    // Hard Constraint: Employee must have the skill required by the shift
    Constraint requiredSkill(ConstraintFactory factory) {
        return factory.forEach(Shift.class)
                .filter(shift -> shift.getAssignedEmployee() != null)
                .filter(shift -> !shift.getAssignedEmployee().getPositions().stream().map(Position::getId).collect(Collectors.toSet())
                        .contains(shift.getRequiredPositionId()))
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Missing required skill");
    }

    //SOFT: Heavily penalize unassigned shifts so the solver tries to fill them
    Constraint minimizeUnassignedShifts(ConstraintFactory factory) {
        return factory.forEachIncludingUnassigned(Shift.class)
                .filter(shift -> shift.getAssignedEmployee() == null)
                .penalize(HardSoftScore.ofSoft(10)) // Penalty of 10 points per empty shift
                .asConstraint("Shift unassigned");
    }

    // Hard Constraint: Employee cannot be in two places at once
    Constraint noOverlappingShifts(ConstraintFactory factory) {
        return factory.forEachUniquePair(Shift.class,
                        Joiners.equal(Shift::getAssignedEmployee),
                        Joiners.overlapping(Shift::getStartTime, Shift::getEndTime))
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Overlapping shifts");
    }

    // Soft (or Hard) Constraint: Max working hours per employee
    Constraint maxWorkingHours(ConstraintFactory factory) {
        return factory.forEach(Shift.class)
                .filter(shift -> shift.getAssignedEmployee() != null)
                .groupBy(Shift::getAssignedEmployee,
                        ConstraintCollectors.sumLong(Shift::getDurationInMinutes))
                .filter((employee, totalMinutes) -> totalMinutes > (employee.getMaxWeeklyHours() * 60L))
                .penalize(HardSoftScore.ONE_HARD,
                        (employee, totalMinutes) -> (int) (totalMinutes - (employee.getMaxWeeklyHours() * 60)))
                .asConstraint("Max working hours exceeded");
    }

    public Constraint employeeUnavailable(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(Shift.class)
                .join(EmployeeAvailability.class,
                        // Match the shift's assigned employee...
                        Joiners.equal(Shift::getAssignedEmployee, EmployeeAvailability::getEmployee),
                        // ...and ensure the dates match (Shift date == Availability date)
                        Joiners.filtering((shift, availability) ->
                                shift.getStartTime().toLocalDate().equals(availability.getDate())
                        ))
                .filter((shift, availability) -> availability.getType() == AvailabilityType.UNAVAILABLE)
                .penalize(HardSoftScore.ONE_HARD) // Hard constraint: Breaks the schedule if violated
                .asConstraint("Employee is unavailable (Vacation or Holiday)");
    }

    Constraint employeeDesired(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(Shift.class)
                // Join Shift with Availability where Employee matches...
                .join(EmployeeAvailability.class,
                        Joiners.equal(Shift::getAssignedEmployee, EmployeeAvailability::getEmployee),
                        // ...and the Date matches
                        Joiners.filtering((shift, availability) ->
                                shift.getStartTime().toLocalDate().equals(availability.getDate())))
                // Only care if the type is DESIRED
                .filter((shift, availability) -> availability.getType() == AvailabilityType.DESIRED)
                // REWARD the score (add points) instead of penalizing
                .reward(HardSoftScore.ONE_SOFT)
                .asConstraint("Employee desires day");
    }
}
