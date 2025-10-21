package com.example.empsched.employee.service.impl;

import com.example.empsched.employee.entity.Organisation;
import com.example.empsched.employee.entity.Position;
import com.example.empsched.employee.exception.OrganisationNotFoundException;
import com.example.empsched.employee.repository.OrganisationRepository;
import com.example.empsched.employee.repository.PositionRepository;
import com.example.empsched.employee.service.PositionService;
import com.example.empsched.shared.utils.BaseThrowChecks;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class PositionServiceImpl implements PositionService {
    private final PositionRepository positionRepository;
    private final OrganisationRepository organisationRepository;

    @Override
    public Position createPosition(Position position, UUID organisationId) {
        final Organisation organisation = organisationRepository.findById(organisationId).orElseThrow(() -> new OrganisationNotFoundException(organisationId));
        position.setOrganisation(organisation);
        return positionRepository.save(position);
    }

    @Override
    public void deletePosition(UUID callerOrganisationId, UUID positionId) {
        Optional<Position> position = positionRepository.findById(positionId);
        if (position.isEmpty()) return;
        // TODO add checks for linked employees (?)
        BaseThrowChecks.throwIfNotRelated(callerOrganisationId, position.get().getOrganisation().getId());
        positionRepository.deleteById(positionId);
    }
}
