package com.example.empsched.schedulingservice.constraints;

import ai.timefold.solver.core.api.score.buildin.hardsoft.HardSoftScore;
import ai.timefold.solver.core.api.score.stream.*;
import com.example.empsched.schedulingservice.entity.Shift;


public class ScheduleConstraintProvider implements ConstraintProvider {

    @Override
    public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[] {
                requiredSkill(constraintFactory),
                maxWorkingHours(constraintFactory),
                noOverlappingShifts(constraintFactory)
        };
    }

    // Hard Constraint: Employee must have the skill required by the shift
    Constraint requiredSkill(ConstraintFactory factory) {
        return factory.forEach(Shift.class)
                .filter(shift -> shift.getAssignedEmployee() != null)
                .filter(shift -> !shift.getAssignedEmployee().getSkills()
                        .contains(shift.getRequiredSkill()))
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Missing required skill");
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
}
