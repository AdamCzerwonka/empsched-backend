package com.example.empsched.organisation.service.impl;

import com.example.empsched.organisation.entity.Organisation;
import com.example.empsched.organisation.entity.Position;
import com.example.empsched.organisation.exception.OrganisationNotFoundException;
import com.example.empsched.organisation.exception.PositionAlreadyExistsException;
import com.example.empsched.organisation.repository.OrganisationRepository;
import com.example.empsched.organisation.repository.PositionRepository;
import com.example.empsched.organisation.service.PositionService;
import com.example.empsched.shared.utils.BaseThrowChecks;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PositionServiceImpl implements PositionService {
    private final PositionRepository positionRepository;
    private final OrganisationRepository organisationRepository;

    @Override
    public List<Position> getOrganisationPositions(final UUID organisationId) {
        return positionRepository.findAllByOrganisationId(organisationId);
    }

    @Override
    public Position createPosition(final Position position, final UUID organisationId) {
        final Organisation organisation = organisationRepository.findById(organisationId)
                .orElseThrow(() -> new OrganisationNotFoundException(organisationId));
        positionRepository.findByNameAndOrganisationId(position.getName(), organisationId)
                .ifPresent(pos -> {
                    throw new PositionAlreadyExistsException(pos.getName());
                });
        position.setOrganisation(organisation);
        return positionRepository.save(position);
    }

    @Override
    public void deletePosition(final UUID positionId, final UUID callerOrganisationId) {
        final Optional<Position> position = positionRepository.findById(positionId);
        if (position.isEmpty()) return;
        // TODO add checks for linked employees (?)
        BaseThrowChecks.throwIfNotRelated(callerOrganisationId, position.get().getOrganisation().getId());
        positionRepository.deleteById(positionId);
    }
}
