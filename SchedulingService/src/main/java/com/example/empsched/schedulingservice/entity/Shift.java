package com.example.empsched.schedulingservice.entity;

import ai.timefold.solver.core.api.domain.entity.PlanningEntity;
import ai.timefold.solver.core.api.domain.lookup.PlanningId;
import ai.timefold.solver.core.api.domain.variable.PlanningVariable;
import com.example.empsched.shared.entity.AbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@PlanningEntity
@Entity
@Getter
@Setter
public class Shift extends AbstractEntity {


    @Column(nullable = false, name = "START_TIME")
    private LocalDateTime startTime;
    @Column(nullable = false, name = "END_TIME")
    private LocalDateTime endTime;
    private String requiredSkill;

    // This is the field Timefold will modify
    @PlanningVariable(valueRangeProviderRefs = "employeeRange")
    @ManyToOne
    private SchedulingEmployee assignedEmployee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SCHEDULE_ID") // Foreign Key in DB
    private Schedule schedule;

    public long getDurationInMinutes() {
        return Duration.between(startTime, endTime).toMinutes();
    }

    @PlanningId
    @Override
    public UUID getId() {
        return super.getId();
    }
}