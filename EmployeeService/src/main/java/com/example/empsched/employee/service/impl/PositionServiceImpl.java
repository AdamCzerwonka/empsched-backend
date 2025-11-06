package com.example.empsched.employee.service.impl;

import com.example.empsched.employee.entity.Employee;
import com.example.empsched.employee.entity.Organisation;
import com.example.empsched.employee.entity.Position;
import com.example.empsched.employee.exception.EmployeeNotFoundException;
import com.example.empsched.employee.exception.OrganisationNotFoundException;
import com.example.empsched.employee.exception.PositionNotFoundException;
import com.example.empsched.employee.repository.EmployeeRepository;
import com.example.empsched.employee.repository.OrganisationRepository;
import com.example.empsched.employee.repository.PositionRepository;
import com.example.empsched.employee.service.PositionService;
import com.example.empsched.shared.util.BaseThrowChecks;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class PositionServiceImpl implements PositionService {
    private final EmployeeRepository employeeRepository;
    private final PositionRepository positionRepository;
    private final OrganisationRepository organisationRepository;

    @Override
    public Position createPosition(final Position position, final UUID organisationId) {
        final Organisation organisation = organisationRepository.findById(organisationId).orElseThrow(() -> new OrganisationNotFoundException(organisationId));
        position.setOrganisation(organisation);
        return positionRepository.save(position);
    }

    @Override
    public List<Position> getEmployeePositions(final UUID callerOrganisationId, final UUID employeeId) {
        final Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new EmployeeNotFoundException(employeeId));
        BaseThrowChecks.throwIfNotRelated(callerOrganisationId, employee.getOrganisation().getId());

        return employee.getPositions().stream().toList();
    }

    @Override
    public List<Position> addPositionToEmployee(final UUID callerOrganisationId, final UUID employeeId, final UUID positionId) {
        final Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new EmployeeNotFoundException(employeeId));
        BaseThrowChecks.throwIfNotRelated(callerOrganisationId, employee.getOrganisation().getId());

        final Position position = positionRepository.findById(positionId).orElseThrow(() -> new PositionNotFoundException(positionId));
        BaseThrowChecks.throwIfNotRelated(callerOrganisationId, position.getOrganisation().getId());

        employee.getPositions().add(position);
        return employeeRepository.save(employee).getPositions().stream().toList();
    }

    @Override
    public List<Position> removePositionFromEmployee(UUID callerOrganisationId, UUID employeeId, UUID positionId) {
        final Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new EmployeeNotFoundException(employeeId));
        BaseThrowChecks.throwIfNotRelated(callerOrganisationId, employee.getOrganisation().getId());

        final Position position = positionRepository.findById(positionId).orElseThrow(() -> new PositionNotFoundException(positionId));
        BaseThrowChecks.throwIfNotRelated(callerOrganisationId, position.getOrganisation().getId());

        employee.getPositions().remove(position);
        return employeeRepository.save(employee).getPositions().stream().toList();
    }

    @Override
    public void deletePosition(final UUID callerOrganisationId, final UUID positionId) {
        Optional<Position> position = positionRepository.findById(positionId);
        if (position.isEmpty()) return;
        // TODO add checks for linked employees (?)
        BaseThrowChecks.throwIfNotRelated(callerOrganisationId, position.get().getOrganisation().getId());
        positionRepository.deleteById(positionId);
    }
}
