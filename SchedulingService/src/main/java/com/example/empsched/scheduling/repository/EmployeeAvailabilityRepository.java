package com.example.empsched.scheduling.repository;

import com.example.empsched.scheduling.entity.AvailabilityType;
import com.example.empsched.scheduling.entity.EmployeeAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface EmployeeAvailabilityRepository extends JpaRepository<EmployeeAvailability, UUID> {
    List<EmployeeAvailability> findAllByDateBetween(LocalDate startDate, LocalDate endDate);

    List<EmployeeAvailability> findAllByEmployeeId(UUID employeeId);

    void deleteByEmployeeIdAndDateBetweenAndTypeIs(UUID employeeId, LocalDate dateAfter, LocalDate dateBefore, AvailabilityType type);


}
