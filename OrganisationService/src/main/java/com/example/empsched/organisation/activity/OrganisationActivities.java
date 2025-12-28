package com.example.empsched.organisation.activity;

import com.example.empsched.organisation.entity.Organisation;
import com.example.empsched.shared.dto.organisation.CreateOrganisationRequest;
import com.example.empsched.shared.dto.organisation.CreateOrganisationWithOwnerRequest;
import com.example.empsched.shared.dto.organisation.OrganisationResponse;
import com.example.empsched.shared.util.RequestContext;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

import java.util.UUID;

@ActivityInterface
public interface OrganisationActivities {

    @ActivityMethod
    OrganisationResponse createOrganisationWithOwnerInEmployeeService(final CreateOrganisationWithOwnerRequest request, final UUID organisationId, final UUID ownerId);

    @ActivityMethod
    Organisation createOrganisationInOrganisationService(final Organisation organisation);

    @ActivityMethod
    void deleteOrganisationInEmployeeService(final UUID id);

    @ActivityMethod
    void deleteOrganisationInOrganisationService(final UUID id);

    @ActivityMethod
    OrganisationResponse createOrganisationInSchedulingService(final CreateOrganisationRequest request);

    @ActivityMethod
    void deleteOrganisationInSchedulingService(final UUID organisationId);
}
