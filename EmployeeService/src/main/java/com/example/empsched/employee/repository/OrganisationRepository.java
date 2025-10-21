package com.example.empsched.employee.repository;

import com.example.empsched.employee.entity.Organisation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
public interface OrganisationRepository extends JpaRepository<Organisation, UUID> {
}
