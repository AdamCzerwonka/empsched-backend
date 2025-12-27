package com.example.empsched.scheduling.entity;

import com.example.empsched.shared.entity.AbstractEntity;
import com.example.empsched.shared.entity.OrganisationPlan;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Organisation extends AbstractEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "plan", nullable = false)
    private OrganisationPlan plan;

    public Organisation(UUID id, OrganisationPlan plan) {
        super(id);
        this.plan = plan;
    }
}
