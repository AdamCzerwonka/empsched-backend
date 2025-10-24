package com.example.empsched.organisation.repository;

import com.example.empsched.organisation.entity.Position;
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
public interface PositionRepository extends JpaRepository<Position, UUID> {
    Optional<Position> findByNameAndOrganisationId(String name, UUID organisationId);

    Page<Position> findAllByOrganisationId(UUID organisationId, Pageable pageable);
}
