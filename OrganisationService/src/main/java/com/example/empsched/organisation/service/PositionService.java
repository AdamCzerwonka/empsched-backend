package com.example.empsched.organisation.service;

import com.example.empsched.organisation.entity.Position;

import java.util.List;
import java.util.UUID;

public interface PositionService {
    List<Position> getOrganisationPositions(final UUID organisationId);

    Position createPosition(final Position position, final UUID organisationId);

    void deletePosition(final UUID positionId, final UUID callerOrganisationId);
}
