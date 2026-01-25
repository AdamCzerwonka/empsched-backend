package com.example.empsched.employee.repository;

import com.example.empsched.employee.entity.Absence;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
public interface AbsenceRepository extends JpaRepository<Absence, UUID>, JpaSpecificationExecutor<Absence> {
    @NonNull
    @Override
    @EntityGraph(attributePaths = {"employee", "employee.organisation"})
    Page<Absence> findAll(Specification<Absence> specification, @NonNull Pageable pageable);

    @EntityGraph(attributePaths = {"employee", "employee.organisation"})
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Absence a " +
            "WHERE a.employee.id = :employeeId " +
            "AND (a.startDate <= :endDate AND a.endDate >= :startDate)")
    boolean hasEmployeeCollidingAbsences(UUID employeeId, LocalDate startDate, LocalDate endDate);

    void deleteByIdAndEmployeeIdAndApprovedFalse(UUID absenceId, UUID employeeId);

    @EntityGraph(attributePaths = {"employee", "employee.organisation"})
    @Query("SELECT a FROM Absence a JOIN FETCH a.employee e WHERE a.id = :absenceId AND e.organisation.id = :organisationId")
    Optional<Absence> findByIdWithEmployee(UUID absenceId, UUID organisationId);
}
