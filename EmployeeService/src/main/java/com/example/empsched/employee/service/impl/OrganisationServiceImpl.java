package com.example.empsched.employee.service.impl;

import com.example.empsched.employee.entity.Organisation;
import com.example.empsched.employee.repository.OrganisationRepository;
import com.example.empsched.employee.service.OrganisationService;
import com.example.empsched.shared.dto.OrganisationCreateEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrganisationServiceImpl implements OrganisationService {
    private final OrganisationRepository organisationRepository;

    @Override
    public void createOrganisation(OrganisationCreateEvent event) {
        Organisation organisation = new Organisation(event.id(), event.maxEmployees());
        organisationRepository.save(organisation);
    }
}
