package com.example.empsched.organisation.activity;

import com.example.empsched.organisation.entity.Organisation;
import com.example.empsched.shared.dto.organisation.CreateOrganisationRequest;
import com.example.empsched.shared.dto.organisation.OrganisationResponse;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

import java.util.UUID;

@ActivityInterface
public interface OrganisationActivities {

    @ActivityMethod
    OrganisationResponse createOrganisationInEmployeeService(final CreateOrganisationRequest request);

    @ActivityMethod
    Organisation createOrganisationInOrganisationService(final Organisation organisation);

    @ActivityMethod
    void deleteOrganisationInEmployeeService(final UUID id);

    @ActivityMethod
    void deleteOrganisationInOrganisationService(final UUID id);
}
