package com.example.empsched.employee.service;

import com.example.empsched.employee.entity.Organisation;

import java.util.UUID;

public interface OrganisationService {
    Organisation createOrganisation(final Organisation organisation);

    void deleteOrganisation(final UUID id);
}
