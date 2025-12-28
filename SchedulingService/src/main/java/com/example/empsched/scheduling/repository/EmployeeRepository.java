package com.example.empsched.scheduling.repository;

import com.example.empsched.scheduling.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    List<Employee> findAllByOrganisationId(UUID organisationId);

    int countByOrganisationId(UUID organisationId);
}
