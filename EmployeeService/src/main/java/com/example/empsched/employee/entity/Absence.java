package com.example.empsched.employee.entity;

import com.example.empsched.shared.entity.AbstractEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Absence extends AbstractEntity {
    public Absence() {
        super(UUID.randomUUID());
    }

    public Absence(final String description, final AbsenceReason reason,
                   final Employee employee, final boolean approved,
                   final LocalDate startDate, final LocalDate endDate) {
        super(UUID.randomUUID());
        this.description = description;
        this.reason = reason;
        this.employee = employee;
        this.approved = approved;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Column(name = "description", length = 3000)
    private String description;

    @Enumerated(EnumType.STRING)
    private AbsenceReason reason;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Employee employee;

    @Column(name = "approved", nullable = false)
    private boolean approved;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;
}
