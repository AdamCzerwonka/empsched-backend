package com.example.empsched.workflow.worker.impl;

import com.example.empsched.shared.dto.organisation.CreateOrganisationRequest;
import com.example.empsched.shared.dto.organisation.CreateOrganisationWithOwnerRequest;
import com.example.empsched.shared.dto.user.CreateUserRequest;
import com.example.empsched.shared.entity.Role;
import com.example.empsched.workflow.activity.OrganisationActivities;
import com.example.empsched.workflow.activity.UserActivities;
import com.example.empsched.workflow.util.Tasks;
import com.example.empsched.workflow.worker.CreateOrganisationWithOwnerWorkflow;
import io.temporal.activity.ActivityOptions;
import io.temporal.failure.ActivityFailure;
import io.temporal.spring.boot.WorkflowImpl;
import io.temporal.workflow.Saga;
import io.temporal.workflow.Workflow;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.UUID;

@Slf4j
@WorkflowImpl(workers = Tasks.WORKER_CREATE_ORGANISATION)
public class CreateOrganisationWithOwnerWorkflowImpl implements CreateOrganisationWithOwnerWorkflow {

    private final OrganisationActivities organisationActivities = Workflow.newActivityStub(
            OrganisationActivities.class,
            ActivityOptions.newBuilder()
                    .setStartToCloseTimeout(Duration.ofSeconds(10))
                    .setRetryOptions(io.temporal.common.RetryOptions.newBuilder()
                            .setInitialInterval(Duration.ofSeconds(2))
                            .setMaximumInterval(Duration.ofSeconds(10))
                            .setMaximumAttempts(3)
                            .build())
                    .build()
    );

    private final UserActivities userActivities = Workflow.newActivityStub(
            UserActivities.class,
            ActivityOptions.newBuilder()
                    .setStartToCloseTimeout(Duration.ofSeconds(10))
                    .build()
    );

    @Override
    public void create(final CreateOrganisationWithOwnerRequest request) {
        final Saga saga = new Saga(new Saga.Options.Builder()
                .setParallelCompensation(true)
                .build());

        final UUID userId = UUID.randomUUID();
        final UUID organisationId = UUID.randomUUID();

        final CreateUserRequest createUserRequest = CreateUserRequest
                .builder()
                .id(userId)
                .organisationId(organisationId)
                .email(request.email())
                .password(request.password())
                .role(Role.ORGANISATION_ADMIN).build();

        final CreateOrganisationRequest createOrganisationRequest = CreateOrganisationRequest
                .builder()
                .id(organisationId)
                .name(request.name())
                .ownerId(userId)
                .plan(request.plan())
                .build();

        try {
            saga.addCompensation(() -> organisationActivities.deleteOrganisationInOrganisationService(createOrganisationRequest.id()));
            organisationActivities.createOrganisationInOrganisationService(createOrganisationRequest);

            saga.addCompensation(() -> userActivities.deleteUserInAuthService(createUserRequest.id()));
            userActivities.createUserInAuthService(createUserRequest);

            saga.addCompensation(() -> organisationActivities.deleteOrganisationInEmployeeService(createOrganisationRequest.id()));
            organisationActivities.createOrganisationInEmployeeService(createOrganisationRequest);
            log.info("Organisation with owner created successfully with Organisation ID: {} and User ID: {}", organisationId, userId);
        } catch (ActivityFailure e) {
            log.error("Organisation with owner creation failed: {}", e.getMessage());
            saga.compensate();
            throw e;
        }
    }
}
