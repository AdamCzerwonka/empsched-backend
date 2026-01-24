package com.example.empsched.scheduling.entity;

import ai.timefold.solver.core.api.domain.lookup.PlanningId;
import com.example.empsched.shared.entity.AbstractEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "employee")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Employee extends AbstractEntity {

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "employee_position",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "position_id"),
            uniqueConstraints = @UniqueConstraint(name = "uk_employee_position", columnNames = {"employee_id", "position_id"})
    )
    private Set<Position> positions;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Organisation organisation;

    private int maxWeeklyHours;

    @PlanningId
    @Override
    public UUID getId() {
        return super.getId();
    }

    public Employee(UUID id) {
        super(id);
    }
}
