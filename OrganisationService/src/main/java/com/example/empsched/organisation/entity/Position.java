package com.example.empsched.organisation.entity;

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
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "uk_position_name", columnNames = {"name", "organisation_id"})
})
public class Position extends AbstractEntity {
        @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", length = 5000)
    private String description;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, updatable = false)
    private Organisation organisation;

    public Position(UUID id, String name, String description, Organisation organisation) {
        super(id);
        this.name = name;
        this.description = description;
        this.organisation = organisation;
    }

    public Position(UUID id, String name, Organisation organisation) {
        super(id);
        this.name = name;
        this.organisation = organisation;
    }
}
