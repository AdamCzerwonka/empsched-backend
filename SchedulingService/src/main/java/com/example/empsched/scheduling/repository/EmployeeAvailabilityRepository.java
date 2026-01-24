package com.example.empsched.scheduling.repository;

import com.example.empsched.scheduling.entity.EmployeeAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
public interface EmployeeAvailabilityRepository extends JpaRepository<EmployeeAvailability, UUID> {
    List<EmployeeAvailability> findAllByDateBetween(LocalDate startDate, LocalDate endDate);

    List<EmployeeAvailability> findAllByEmployeeId(UUID employeeId);

    void deleteByAbsenceId(UUID absenceId);


}
