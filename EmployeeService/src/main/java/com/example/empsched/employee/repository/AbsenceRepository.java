package com.example.empsched.employee.repository;

import com.example.empsched.employee.entity.Absence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
public interface AbsenceRepository extends JpaRepository<Absence, UUID>, JpaSpecificationExecutor<Absence> {
    @Query("SELECT a FROM Absence a WHERE a.employee.id = :employeeId " +
            "AND (a.startDate <= :endDate AND a.endDate >= :startDate)")
    List<Absence> findAllCollidingEmployeeAbsence(UUID employeeId, LocalDate startDate, LocalDate endDate);

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Absence a " +
            "WHERE a.employee.id = :employeeId " +
            "AND (a.startDate <= :endDate AND a.endDate >= :startDate)")
    boolean hasEmployeeCollidingAbsences(UUID employeeId, LocalDate startDate, LocalDate endDate);

    void deleteByIdAndEmployeeIdAndApprovedFalse(UUID absenceId, UUID employeeId);

    @Query("SELECT a FROM Absence a JOIN FETCH a.employee e WHERE a.id = :absenceId AND e.organisation.id = :organisationId")
    Optional<Absence> findByIdWithEmployee(UUID absenceId, UUID organisationId);
}
