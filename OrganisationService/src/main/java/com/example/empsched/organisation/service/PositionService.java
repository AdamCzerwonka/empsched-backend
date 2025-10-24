package com.example.empsched.organisation.service;

import com.example.empsched.organisation.entity.Position;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface PositionService {
    Page<Position> getOrganisationPositions(final UUID organisationId, final Pageable pageable);

    Position createPosition(final Position position, final UUID organisationId);

    void deletePosition(final UUID positionId, final UUID callerOrganisationId);
}
