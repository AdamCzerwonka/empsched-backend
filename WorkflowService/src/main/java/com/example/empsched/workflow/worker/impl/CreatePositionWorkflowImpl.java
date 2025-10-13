package com.example.empsched.workflow.worker.impl;

import com.example.empsched.shared.dto.position.CreatePositionRequest;
import com.example.empsched.shared.dto.position.PositionResponse;
import com.example.empsched.workflow.activity.PositionActivities;
import com.example.empsched.workflow.mapper.DtoMapper;
import com.example.empsched.workflow.util.CustomActivityOptions;
import com.example.empsched.workflow.util.RequestContext;
import com.example.empsched.workflow.util.Tasks;
import com.example.empsched.workflow.worker.CreatePositionWorkflow;
import io.temporal.spring.boot.WorkflowImpl;
import io.temporal.workflow.Saga;
import io.temporal.workflow.Workflow;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
@WorkflowImpl(workers = Tasks.WORKER_POSITION_MANAGEMENT)
@RequiredArgsConstructor
public class CreatePositionWorkflowImpl implements CreatePositionWorkflow {
    private final DtoMapper mapper;

    private final PositionActivities positionActivities = Workflow.newActivityStub(
            PositionActivities.class,
            CustomActivityOptions.DEFAULT
    );

    @Override
    public PositionResponse create(final CreatePositionRequest request, final RequestContext context) {
        final Saga saga = new Saga(new Saga.Options.Builder()
                .setParallelCompensation(true)
                .build());

        final UUID positionId = UUID.randomUUID();
        final CreatePositionRequest createPositionRequest = mapper.mapToCreatePositionRequest(positionId, request);

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
