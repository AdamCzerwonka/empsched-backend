package com.example.empsched.scheduling.entity;

import com.example.empsched.shared.entity.AbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
public class Schedule extends AbstractEntityGeneratedId {

    private LocalDate startDate;
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private ScheduleStatus status;

    private String score;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Shift> shiftList;

    @ManyToOne
    @JoinColumn(name = "organisation_id")
    private Organisation organisation;

}