package com.example.empsched.scheduling.repository;

import com.example.empsched.scheduling.entity.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, UUID> {
    List<Shift> findAllByScheduleId(UUID scheduleId);

    void deleteByScheduleId(UUID scheduleId);
}
