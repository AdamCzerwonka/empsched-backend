package com.example.empsched.employee.entity;

import com.example.empsched.shared.entity.AbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Position extends AbstractEntity {
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, updatable = false)
    private Organisation organisation;

    public Position(UUID id, Organisation organisation) {
        super(id);
        this.organisation = organisation;
    }
}
