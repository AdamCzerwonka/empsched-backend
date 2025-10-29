package com.example.empsched.employee.repository;

import com.example.empsched.employee.entity.Absence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
public interface AbsenceRepository extends JpaRepository<Absence, UUID> {
    @Query("SELECT a FROM Absence a WHERE a.employee.id = :employeeId " +
            "AND (a.startDate <= :endDate AND a.endDate >= :startDate)")
    Optional<Absence> findCollidingEmployeeAbsence(UUID employeeId, LocalDate startDate, LocalDate endDate);
}
