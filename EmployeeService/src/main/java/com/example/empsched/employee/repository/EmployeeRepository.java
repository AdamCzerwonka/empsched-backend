package com.example.empsched.employee.repository;

import com.example.empsched.employee.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    Optional<Employee> findByIdAndOrganisationId(UUID id, UUID organisationId);

    Page<Employee> findAllByOrganisationId(UUID organisationId, Pageable pageable);

    void deleteByIdAndOrganisationId(UUID id, UUID organisationId);

    int countByOrganisationId(UUID organisationId);
}
