package com.example.empsched.organisation.repository;

import com.example.empsched.organisation.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PositionRepository extends JpaRepository<Position, UUID> {
    Optional<Position> findByNameAndOrganisationId(String name, UUID organisationId);

    List<Position> findAllByOrganisationId(UUID organisationId);
}
