package com.example.empsched.schedulingservice.repository;

import com.example.empsched.schedulingservice.entity.Shift;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ShiftRepository extends JpaRepository<Shift, UUID> {
    List<Shift> findAllByScheduleId(UUID scheduleId);

    void deleteByScheduleId(UUID scheduleId);
}
