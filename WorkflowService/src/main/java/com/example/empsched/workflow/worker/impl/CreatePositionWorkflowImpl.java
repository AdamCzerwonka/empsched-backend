package com.example.empsched.workflow.worker.impl;

import com.example.empsched.shared.dto.position.CreatePositionRequest;
import com.example.empsched.shared.dto.position.PositionResponse;
import com.example.empsched.workflow.activity.PositionActivities;
import com.example.empsched.workflow.util.RequestContext;
import com.example.empsched.workflow.util.Tasks;
import com.example.empsched.workflow.worker.CreatePositionWorkflow;
import io.temporal.activity.ActivityOptions;
import io.temporal.spring.boot.WorkflowImpl;
import io.temporal.workflow.Saga;
import io.temporal.workflow.Workflow;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.UUID;

@Slf4j
@WorkflowImpl(workers = Tasks.WORKER_POSITION_MANAGEMENT)
public class CreatePositionWorkflowImpl implements CreatePositionWorkflow {

    private final PositionActivities positionActivities = Workflow.newActivityStub(
            PositionActivities.class,
            ActivityOptions.newBuilder()
                    .setStartToCloseTimeout(Duration.ofSeconds(10))
                    .setRetryOptions(io.temporal.common.RetryOptions.newBuilder()
                            .setInitialInterval(Duration.ofSeconds(2))
                            .setMaximumInterval(Duration.ofSeconds(10))
                            .setMaximumAttempts(3)
                            .build())
                    .build()
    );

    @Override
    public PositionResponse create(final CreatePositionRequest request, final RequestContext context) {
        final Saga saga = new Saga(new Saga.Options.Builder()
                .setParallelCompensation(true)
                .build());

        final UUID positionId = UUID.randomUUID();
        final CreatePositionRequest createPositionRequest = CreatePositionRequest
                .builder()
                .id(positionId)
                .name(request.name())
                .description(request.description())
                .build();

        try {
            saga.addCompensation(() -> positionActivities.deletePositionInOrganisationService(positionId, context));
            final PositionResponse positionResponse = positionActivities.createPositionInOrganisationService(createPositionRequest, context);

            saga.addCompensation(() -> positionActivities.deletePositionInEmployeeService(positionId, context));
            positionActivities.createPositionInEmployeeService(createPositionRequest, context);
            log.info("Position created successfully with ID: {}", positionId);
            return positionResponse;
        } catch (Exception e) {
            log.error("Exception occurred during position creation: {}", e.getMessage(), e);
            saga.compensate();
            throw e;
        }
    }
}
