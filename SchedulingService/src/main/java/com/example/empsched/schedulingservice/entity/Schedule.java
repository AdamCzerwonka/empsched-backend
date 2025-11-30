package com.example.empsched.schedulingservice.entity;

import com.example.empsched.shared.entity.AbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table
@Getter
@Setter
public class Schedule extends AbstractEntity {


    // Defines the scope (e.g., Monday to Sunday)
    private LocalDate startDate;
    private LocalDate endDate;

    // Critical for Microservices: manage the state!
    @Enumerated(EnumType.STRING)
    private ScheduleStatus status;

    // Optional: Store the score of the solution for analytics
    private String score;

    // One Schedule has many Shifts
    // 'orphanRemoval = true' ensures if you delete the Schedule, the Shifts go with it.
    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Shift> shiftList;

}