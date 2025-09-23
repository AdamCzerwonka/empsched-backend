package com.example.empsched.employee.service.impl;

import com.example.empsched.employee.entity.Organisation;
import com.example.empsched.employee.repository.OrganisationRepository;
import com.example.empsched.employee.service.OrganisationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrganisationServiceImpl implements OrganisationService {
    private final OrganisationRepository organisationRepository;

    @Override
    public Organisation createOrganisation(final Organisation organisation) {
        return organisationRepository.save(organisation);
    }

    @Override
    public void deleteOrganisation(final UUID id) {
        // TODO handle cascade delete or constraints (e.g., check for associated employees)
        organisationRepository.deleteById(id);
    }
}
