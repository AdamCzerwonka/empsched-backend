package com.example.empsched.scheduling.entity;

import com.example.empsched.shared.entity.AbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_EMPLOYEE_DATE",
                        columnNames = {"EMPLOYEE_ID", "DATE"}
                )
        }
)
public class EmployeeAvailability extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "EMPLOYEE_ID")
    private Employee employee;

    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private AvailabilityType type;

    UUID absenceId;
}