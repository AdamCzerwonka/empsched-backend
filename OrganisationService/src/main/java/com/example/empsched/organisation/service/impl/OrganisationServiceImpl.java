package com.example.empsched.organisation.service.impl;

import com.example.empsched.organisation.entity.Organisation;
import com.example.empsched.organisation.exception.OrganisationAlreadyExistsException;
import com.example.empsched.organisation.repository.OrganisationRepository;
import com.example.empsched.organisation.service.OrganisationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrganisationServiceImpl implements OrganisationService {
    private final OrganisationRepository organisationRepository;

    @Override
    public Organisation createOrganisation(final Organisation organisation) {

        organisationRepository.findByName(organisation.getName())
                .ifPresent(a -> {
                    throw new OrganisationAlreadyExistsException(a.getName());
                });
        return organisationRepository.save(organisation);
    }

    @Override
    public void deleteOrganisation(final UUID id) {
       organisationRepository.deleteById(id);
    }
}
