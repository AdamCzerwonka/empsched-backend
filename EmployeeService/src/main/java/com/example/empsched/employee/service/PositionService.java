package com.example.empsched.employee.service;

import com.example.empsched.employee.entity.Position;

import java.util.UUID;

public interface PositionService {
    Position createPosition(final Position position, final UUID organisationId);

    void deletePosition(final UUID callerOrganisationId, final UUID positionId);
}
