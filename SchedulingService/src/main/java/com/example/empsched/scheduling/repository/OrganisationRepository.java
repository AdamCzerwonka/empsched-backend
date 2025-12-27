package com.example.empsched.scheduling.repository;

import com.example.empsched.scheduling.entity.Organisation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrganisationRepository extends JpaRepository<Organisation, UUID> {
}

