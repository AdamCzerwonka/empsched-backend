package com.example.empsched.organisation.workflow.impl;

import com.example.empsched.organisation.activity.PositionActivities;
import com.example.empsched.organisation.entity.Position;
import com.example.empsched.organisation.mapper.DtoMapper;
import com.example.empsched.organisation.mapper.RequestMapper;
import com.example.empsched.organisation.util.WorkflowTasks;
import com.example.empsched.organisation.workflow.CreatePositionWorkflow;
import com.example.empsched.shared.dto.position.CreatePositionRequest;
import com.example.empsched.shared.util.CustomActivityOptions;
import com.example.empsched.shared.util.RequestContext;
import io.temporal.spring.boot.WorkflowImpl;
import io.temporal.workflow.Saga;
import io.temporal.workflow.Workflow;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
@WorkflowImpl(workers = WorkflowTasks.WORKER_POSITION_MANAGEMENT)
@RequiredArgsConstructor
public class CreatePositionWorkflowImpl implements CreatePositionWorkflow {
    private final DtoMapper dtoMapper;
    private final RequestMapper mapper;

    private final PositionActivities positionActivities = Workflow.newActivityStub(
            PositionActivities.class,
            CustomActivityOptions.DEFAULT
    );

    @Override
    public Position create(final CreatePositionRequest request, final RequestContext context) {
        final Saga saga = new Saga(new Saga.Options.Builder()
                .setParallelCompensation(true)
                .build());

        final UUID positionId = UUID.randomUUID();
        final CreatePositionRequest createPositionRequest = mapper.mapToCreatePositionRequest(positionId, request);

        try {
            saga.addCompensation(() -> positionActivities.deletePositionInOrganisationService(positionId, context));
            final Position position = positionActivities.createPositionInOrganisationService(dtoMapper.mapToPosition(createPositionRequest), context);

            saga.addCompensation(() -> positionActivities.deletePositionInEmployeeService(positionId, context));
            positionActivities.createPositionInEmployeeService(createPositionRequest, context);
            log.info("Position created successfully with ID: {}", positionId);
            return position;
        } catch (Exception e) {
            log.error("Exception occurred during position creation: {}", e.getMessage(), e);
            saga.compensate();
            throw e;
        }
    }
}
