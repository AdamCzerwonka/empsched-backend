package com.example.empsched.workflow.worker.impl;

import com.example.empsched.shared.dto.organisation.CreateOrganisationRequest;
import com.example.empsched.shared.dto.organisation.CreateOrganisationWithOwnerRequest;
import com.example.empsched.shared.dto.organisation.OrganisationResponse;
import com.example.empsched.shared.dto.user.CreateUserRequest;
import com.example.empsched.shared.entity.Role;
import com.example.empsched.workflow.activity.OrganisationActivities;
import com.example.empsched.workflow.activity.UserActivities;
import com.example.empsched.workflow.mapper.DtoMapper;
import com.example.empsched.workflow.util.CustomActivityOptions;
import com.example.empsched.workflow.util.Tasks;
import com.example.empsched.workflow.worker.CreateOrganisationWithOwnerWorkflow;
import io.temporal.failure.ActivityFailure;
import io.temporal.spring.boot.WorkflowImpl;
import io.temporal.workflow.Saga;
import io.temporal.workflow.Workflow;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
@WorkflowImpl(workers = Tasks.WORKER_CREATE_ORGANISATION)
@RequiredArgsConstructor
public class CreateOrganisationWithOwnerWorkflowImpl implements CreateOrganisationWithOwnerWorkflow {
    private final DtoMapper mapper;

    private final OrganisationActivities organisationActivities = Workflow.newActivityStub(
            OrganisationActivities.class,
            CustomActivityOptions.DEFAULT
    );

    private final UserActivities userActivities = Workflow.newActivityStub(
            UserActivities.class,
            CustomActivityOptions.DEFAULT
    );

    @Override
    public OrganisationResponse create(final CreateOrganisationWithOwnerRequest request) {
        final Saga saga = new Saga(new Saga.Options.Builder()
                .setParallelCompensation(true)
                .build());

        final UUID userId = UUID.randomUUID();
        final UUID organisationId = UUID.randomUUID();

        final CreateUserRequest createUserRequest = mapper.mapToCreateUserRequest(userId, organisationId, Role.ORGANISATION_ADMIN, request);
        final CreateOrganisationRequest createOrganisationRequest = mapper.mapToCreateOrganisationRequest(organisationId, userId, request);

        try {
            saga.addCompensation(() -> organisationActivities.deleteOrganisationInOrganisationService(createOrganisationRequest.id()));
            final OrganisationResponse organisationResponse = organisationActivities.createOrganisationInOrganisationService(createOrganisationRequest);

            saga.addCompensation(() -> userActivities.deleteUserInAuthService(createUserRequest.id()));
            userActivities.createUserInAuthService(createUserRequest);

            saga.addCompensation(() -> organisationActivities.deleteOrganisationInEmployeeService(createOrganisationRequest.id()));
            organisationActivities.createOrganisationInEmployeeService(createOrganisationRequest);
            log.info("Organisation with owner created successfully with Organisation ID: {} and User ID: {}", organisationId, userId);
            return organisationResponse;
        } catch (ActivityFailure e) {
            log.error("Organisation with owner creation failed: {}", e.getMessage());
            saga.compensate();
            throw e;
        }
    }
}
