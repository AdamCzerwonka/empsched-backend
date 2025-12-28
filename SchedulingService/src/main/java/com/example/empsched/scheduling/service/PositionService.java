package com.example.empsched.scheduling.service;

import com.example.empsched.scheduling.entity.Employee;
import com.example.empsched.scheduling.entity.Organisation;
import com.example.empsched.scheduling.entity.Position;
import com.example.empsched.scheduling.exceptions.EmployeeNotFoundException;
import com.example.empsched.scheduling.exceptions.OrganisationNotFoundException;
import com.example.empsched.scheduling.exceptions.PositionNotFoundException;
import com.example.empsched.scheduling.repository.EmployeeRepository;
import com.example.empsched.scheduling.repository.OrganisationRepository;
import com.example.empsched.scheduling.repository.PositionRepository;
import com.example.empsched.shared.util.BaseThrowChecks;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PositionService {

    private final PositionRepository positionRepository;
    private final EmployeeRepository employeeRepository;
    private final OrganisationRepository organisationRepository;

    public Position createPosition(final Position position, final UUID organisationId) {
        final Organisation organisation = organisationRepository.findById(organisationId).orElseThrow(() -> new OrganisationNotFoundException(organisationId));
        position.setOrganisation(organisation);
        return positionRepository.save(position);
    }

    public void deletePosition(UUID positionId, UUID callerOrganisationId) {
        Optional<Position> position = positionRepository.findById(positionId);
        if (position.isEmpty()) return; // Throw exception?
        // TODO add checks for linked employees (?)
        BaseThrowChecks.throwIfNotRelated(callerOrganisationId, position.get().getOrganisation().getId());
        positionRepository.deleteById(positionId);
    }

    public List<Position> getEmployeePositions(final UUID callerOrganisationId, final UUID employeeId) {
        final Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new EmployeeNotFoundException(employeeId));
        BaseThrowChecks.throwIfNotRelated(callerOrganisationId, employee.getOrganisation().getId());

        return employee.getPositions().stream().toList();
    }

    public List<Position> addPositionToEmployee(final UUID callerOrganisationId, final UUID employeeId, final UUID positionId) {
        final Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new EmployeeNotFoundException(employeeId));
        BaseThrowChecks.throwIfNotRelated(callerOrganisationId, employee.getOrganisation().getId());

        final Position position = positionRepository.findById(positionId).orElseThrow(() -> new PositionNotFoundException(positionId));
        BaseThrowChecks.throwIfNotRelated(callerOrganisationId, position.getOrganisation().getId());

        employee.getPositions().add(position);
        return employeeRepository.save(employee).getPositions().stream().toList();
    }

    public List<Position> removePositionFromEmployee(UUID callerOrganisationId, UUID employeeId, UUID positionId) {
        final Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found: " + employeeId));
        BaseThrowChecks.throwIfNotRelated(callerOrganisationId, employee.getOrganisation().getId());

        final Position position = positionRepository.findById(positionId)
                .orElseThrow(() -> new IllegalArgumentException("Position not found: " + positionId));
        BaseThrowChecks.throwIfNotRelated(callerOrganisationId, position.getOrganisation().getId());

        employee.getPositions().remove(position);
        return employeeRepository.save(employee).getPositions().stream().toList();
    }


}
