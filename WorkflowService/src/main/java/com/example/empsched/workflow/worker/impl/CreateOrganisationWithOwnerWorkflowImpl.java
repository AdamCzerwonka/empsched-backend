package com.example.empsched.workflow.worker.impl;

import com.example.empsched.shared.dto.organisation.CreateOrganisationRequest;
import com.example.empsched.shared.dto.organisation.CreateOrganisationWithOwnerRequest;
import com.example.empsched.shared.dto.user.CreateUserRequest;
import com.example.empsched.shared.entity.Role;
import com.example.empsched.workflow.activity.CreateActivities;
import com.example.empsched.workflow.activity.DeleteActivities;
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

    private final CreateActivities createActivities = Workflow.newActivityStub(
            CreateActivities.class,
            ActivityOptions.newBuilder()
                    .setStartToCloseTimeout(Duration.ofSeconds(10))
                    .setRetryOptions(io.temporal.common.RetryOptions.newBuilder()
                            .setInitialInterval(Duration.ofSeconds(2))
                            .setMaximumInterval(Duration.ofSeconds(10))
                            .setMaximumAttempts(3)
                            .build())
                    .build()
    );

    private final DeleteActivities deleteActivities = Workflow.newActivityStub(
            DeleteActivities.class,
            ActivityOptions.newBuilder()
                    .setStartToCloseTimeout(Duration.ofSeconds(10))
                    .build()
    );

    @Override
    public void create(final CreateOrganisationWithOwnerRequest request) {
        final Saga saga = new Saga(new Saga.Options.Builder()
                .setParallelCompensation(true)
                .build());

        final CreateUserRequest createUserRequest = CreateUserRequest
                .builder()
                .id(UUID.randomUUID())
                .email(request.email())
                .password(request.password())
                .role(Role.ORGANISATION_ADMIN).build();

        final CreateOrganisationRequest createOrganisationRequest = CreateOrganisationRequest
                .builder()
                .id(UUID.randomUUID())
                .name(request.name())
                .maxEmployees(request.maxEmployees())
                .ownerId(createUserRequest.id())
                .build();

        try {
            saga.addCompensation(() -> deleteActivities.deleteOrganisationInOrganisationService(createOrganisationRequest.id()));
            createActivities.createOrganisationInOrganisationService(createOrganisationRequest);
            saga.addCompensation(() -> deleteActivities.deleteUserInAuthService(createUserRequest.id()));
            createActivities.createUserInAuthService(createUserRequest);
            saga.addCompensation(() -> deleteActivities.deleteOrganisationInEmployeeService(createOrganisationRequest.id()));
            createActivities.createOrganisationInEmployeeService(createOrganisationRequest);
            log.info("Organisation with owner creation completed successfully.");
        } catch (ActivityFailure e) {
            log.error("Organisation with owner creation failed: {}", e.getMessage());
            log.error("Starting compensation transactions.");
            saga.compensate();
            throw e;
        }
    }
}
