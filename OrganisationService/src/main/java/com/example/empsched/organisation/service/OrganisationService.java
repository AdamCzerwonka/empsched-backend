package com.example.empsched.organisation.service;


import com.example.empsched.organisation.entity.Organisation;

import java.util.UUID;

public interface OrganisationService {
    Organisation createOrganisation(final Organisation organisation);

    void deleteOrganisation(final UUID id);
}
