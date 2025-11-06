package com.example.empsched.organisation.activity.impl;

import com.example.empsched.organisation.activity.PositionActivities;
import com.example.empsched.organisation.entity.Position;
import com.example.empsched.organisation.service.PositionService;
import com.example.empsched.organisation.util.WorkflowTasks;
import com.example.empsched.shared.dto.position.CreatePositionRequest;
import com.example.empsched.shared.dto.position.PositionResponse;
import com.example.empsched.shared.client.EmployeeServiceClient;
import com.example.empsched.shared.util.RequestContext;
import io.temporal.spring.boot.ActivityImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@ActivityImpl(workers = {WorkflowTasks.WORKER_POSITION_MANAGEMENT})
@RequiredArgsConstructor
public class PositionActivitiesImpl implements PositionActivities {
    private final EmployeeServiceClient employeeServiceClient;
    private final PositionService positionService;

    @Override
    public PositionResponse createPositionInEmployeeService(final CreatePositionRequest request, final RequestContext context) {
        return employeeServiceClient.createPosition(request, context).getBody();
    }

    @Override
    public Position createPositionInOrganisationService(final Position position, final RequestContext context) {
        return positionService.createPosition(position, context.getOrganisationId());
    }

    @Override
    public void deletePositionInEmployeeService(final UUID positionId, final RequestContext context) {
        employeeServiceClient.deletePosition(positionId, context);
    }

    @Override
    public void deletePositionInOrganisationService(final UUID positionId, final RequestContext context) {
        positionService.deletePosition(positionId, context.getOrganisationId());
    }
}
