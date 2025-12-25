package com.example.empsched.schedulingservice.repository;

import com.example.empsched.schedulingservice.entity.EmployeeAvailability;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface EmployeeAvailabilityRepository extends JpaRepository<EmployeeAvailability, UUID> {
    List<EmployeeAvailability> findAllByDateBetween(LocalDate startDate, LocalDate endDate);
}
