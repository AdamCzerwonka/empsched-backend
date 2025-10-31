package com.example.empsched.organisation.workflow.impl;

import com.example.empsched.organisation.activity.OrganisationActivities;
import com.example.empsched.organisation.activity.UserActivities;
import com.example.empsched.organisation.entity.Organisation;
import com.example.empsched.organisation.mapper.DtoMapper;
import com.example.empsched.organisation.mapper.RequestMapper;
import com.example.empsched.organisation.util.WorkflowTasks;
import com.example.empsched.organisation.workflow.CreateOrganisationWithOwnerWorkflow;
import com.example.empsched.shared.dto.organisation.CreateOrganisationRequest;
import com.example.empsched.shared.dto.organisation.CreateOrganisationWithOwnerRequest;
import com.example.empsched.shared.dto.user.CreateUserRequest;
import com.example.empsched.shared.entity.Role;
import com.example.empsched.shared.utils.CustomActivityOptions;
import io.temporal.failure.ActivityFailure;
import io.temporal.spring.boot.WorkflowImpl;
import io.temporal.workflow.Saga;
import io.temporal.workflow.Workflow;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
@WorkflowImpl(workers = WorkflowTasks.WORKER_ORGANISATION_MANAGEMENT)
@RequiredArgsConstructor
public class CreateOrganisationWithOwnerWorkflowImpl implements CreateOrganisationWithOwnerWorkflow {
    private final DtoMapper dtoMapper;
    private final RequestMapper requestMapper;

    private final OrganisationActivities organisationActivities = Workflow.newActivityStub(
            OrganisationActivities.class,
            CustomActivityOptions.DEFAULT
    );

    private final UserActivities userActivities = Workflow.newActivityStub(
            UserActivities.class,
            CustomActivityOptions.DEFAULT
    );

    @Override
    public Organisation create(final CreateOrganisationWithOwnerRequest request) {
        final Saga saga = new Saga(new Saga.Options.Builder()
                .setParallelCompensation(true)
                .build());

        final UUID userId = UUID.randomUUID();
        final UUID organisationId = UUID.randomUUID();

        final CreateUserRequest createUserRequest = requestMapper.mapToCreateUserRequest(userId, organisationId, Role.ORGANISATION_ADMIN, request);
        final CreateOrganisationRequest createOrganisationRequest = requestMapper.mapToCreateOrganisationRequest(organisationId, userId, request);

        try {
            saga.addCompensation(() -> organisationActivities.deleteOrganisationInOrganisationService(createOrganisationRequest.id()));
            final Organisation organisationResponse = organisationActivities.createOrganisationInOrganisationService(dtoMapper.mapToOrganisation(createOrganisationRequest));

            saga.addCompensation(() -> userActivities.deleteUserInAuthService(createUserRequest.id()));
            userActivities.createUserInAuthService(createUserRequest);

            saga.addCompensation(() -> organisationActivities.deleteOrganisationInEmployeeService(createOrganisationRequest.id()));
            organisationActivities.createOrganisationWithOwnerInEmployeeService(request, organisationId, userId);
            log.info("Organisation with owner created successfully with Organisation ID: {} and User ID: {}", organisationId, userId);
            return organisationResponse;
        } catch (ActivityFailure e) {
            log.error("Organisation with owner creation failed: {}", e.getMessage());
            saga.compensate();
            throw e;
        }
    }
}
