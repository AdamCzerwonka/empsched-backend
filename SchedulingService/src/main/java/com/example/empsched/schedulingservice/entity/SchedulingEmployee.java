package com.example.empsched.schedulingservice.entity;

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
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SchedulingEmployee extends AbstractSchedulingEntity {

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> skills;

//    @Enumerated(EnumType.STRING)
//    private ContractType contractType;

    private int maxWeeklyHours;

    @PlanningId
    @Override
    public UUID getId() {
        return super.getId();
    }

}

