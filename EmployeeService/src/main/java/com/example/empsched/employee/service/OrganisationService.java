package com.example.empsched.employee.service;

import com.example.empsched.shared.dto.OrganisationCreateEvent;

public interface OrganisationService {
    void createOrganisation(OrganisationCreateEvent event);
}
