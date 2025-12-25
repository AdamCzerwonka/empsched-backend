package com.example.empsched.schedulingservice.schedule;

import ai.timefold.solver.core.api.domain.solution.PlanningEntityCollectionProperty;
import ai.timefold.solver.core.api.domain.solution.PlanningScore;
import ai.timefold.solver.core.api.domain.solution.PlanningSolution;
import ai.timefold.solver.core.api.domain.solution.ProblemFactCollectionProperty;
import ai.timefold.solver.core.api.domain.valuerange.ValueRangeProvider;
import ai.timefold.solver.core.api.score.buildin.hardsoft.HardSoftScore;
import com.example.empsched.schedulingservice.entity.EmployeeAvailability;
import com.example.empsched.schedulingservice.entity.SchedulingEmployee;
import com.example.empsched.schedulingservice.entity.Shift;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;


@PlanningSolution
@Getter
@NoArgsConstructor
public class WorkSchedule {

    private UUID scheduleId;

    @PlanningScore
    private HardSoftScore score;

    @ProblemFactCollectionProperty
    @ValueRangeProvider(id = "employeeRange")
    private List<SchedulingEmployee> employeeList;

    @PlanningEntityCollectionProperty
    private List<Shift> shiftList;

    @ProblemFactCollectionProperty
    private List<EmployeeAvailability> availabilityList;

    public WorkSchedule(UUID scheduleId,
                        List<SchedulingEmployee> employeeList,
                        List<Shift> shiftList,
                        List<EmployeeAvailability> availabilityList) {
        this.scheduleId = scheduleId;
        this.employeeList = employeeList;
        this.shiftList = shiftList;
        this.availabilityList = availabilityList;
    }

}

