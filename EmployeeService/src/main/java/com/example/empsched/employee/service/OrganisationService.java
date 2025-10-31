package com.example.empsched.employee.service;

import com.example.empsched.employee.entity.Employee;
import com.example.empsched.employee.entity.Organisation;

import java.util.UUID;

public interface OrganisationService {
    Organisation createOrganisationWithOwner(final Organisation organisation, final Employee owner);

    void deleteOrganisation(final UUID id);
}
