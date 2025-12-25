package com.example.empsched.schedulingservice.entity;

import com.example.empsched.shared.entity.AbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class EmployeeAvailability extends AbstractSchedulingEntity {

    public EmployeeAvailability(SchedulingEmployee employee, LocalDate date, AvailabilityType type) {
        this.employee = employee;
        this.date = date;
        this.type = type;
    }

    @ManyToOne
    @JoinColumn(name = "EMPLOYEE_ID")
    private SchedulingEmployee employee;

    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private AvailabilityType type; // E.g., UNAVAILABLE, DESIRED

    // Helper to check against a shift
    public boolean overlapsWith(LocalDate shiftDate) {
        return this.date.equals(shiftDate);
    }
}