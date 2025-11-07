package com.example.empsched.organisation.service.impl;

import com.example.empsched.organisation.entity.Organisation;
import com.example.empsched.organisation.entity.Position;
import com.example.empsched.organisation.exception.OrganisationNotFoundException;
import com.example.empsched.organisation.exception.PositionAlreadyExistsException;
import com.example.empsched.organisation.repository.OrganisationRepository;
import com.example.empsched.organisation.repository.PositionRepository;
import com.example.empsched.organisation.service.PositionService;
import com.example.empsched.shared.util.BaseThrowChecks;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Page<Position> getOrganisationPositions(final UUID organisationId, final Pageable pageable) {
        return positionRepository.findAllByOrganisationId(organisationId, pageable);
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
        BaseThrowChecks.throwIfNotRelated(callerOrganisationId, position.get().getOrganisation().getId());
        positionRepository.deleteById(positionId);
    }
}
