package com.example.empsched.employee.service;

import com.example.empsched.employee.entity.Position;

import java.util.List;
import java.util.UUID;

public interface PositionService {
    Position createPosition(final Position position, final UUID organisationId);

    List<Position> getEmployeePositions(final UUID callerOrganisationId, final UUID employeeId);

    List<Position> addPositionToEmployee(final UUID callerOrganisationId, final UUID employeeId, final UUID positionId);

    List<Position> removePositionFromEmployee(final UUID callerOrganisationId, final UUID employeeId, final UUID positionId);

    void deletePosition(final UUID callerOrganisationId, final UUID positionId);
}
