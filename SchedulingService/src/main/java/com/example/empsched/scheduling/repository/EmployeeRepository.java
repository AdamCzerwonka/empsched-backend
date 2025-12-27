package com.example.empsched.scheduling.repository;

import com.example.empsched.scheduling.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    Page<Employee> findAllByOrganisationId(UUID organisationId, Pageable pageable);

    int countByOrganisationId(UUID organisationId);
}
