package com.example.empsched.scheduling.entity;

import ai.timefold.solver.core.api.domain.entity.PlanningEntity;
import ai.timefold.solver.core.api.domain.lookup.PlanningId;
import ai.timefold.solver.core.api.domain.variable.PlanningVariable;
import com.example.empsched.shared.entity.AbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@PlanningEntity
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Shift extends AbstractEntityGeneratedId {

    @Column(nullable = false, name = "start_time")
    private LocalDateTime startTime;
    @Column(nullable = false, name = "end_time")
    private LocalDateTime endTime;
    private UUID requiredPositionId;

    @PlanningVariable(valueRangeProviderRefs = "employeeRange", allowsUnassigned = true)
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private Employee assignedEmployee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
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