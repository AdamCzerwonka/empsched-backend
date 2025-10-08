package com.example.empsched.workflow.activity.impl;

import com.example.empsched.shared.dto.position.CreatePositionRequest;
import com.example.empsched.shared.dto.position.PositionResponse;
import com.example.empsched.workflow.activity.PositionActivities;
import com.example.empsched.workflow.client.EmployeeServiceClient;
import com.example.empsched.workflow.client.OrganisationServiceClient;
import com.example.empsched.workflow.util.RequestContext;
import com.example.empsched.workflow.util.Tasks;
import io.temporal.spring.boot.ActivityImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@ActivityImpl(workers = {Tasks.WORKER_POSITION_MANAGEMENT})
@RequiredArgsConstructor
public class PositionActivitiesImpl implements PositionActivities {
    private final OrganisationServiceClient organisationServiceClient;
    private final EmployeeServiceClient employeeServiceClient;

    @Override
    public PositionResponse createPositionInEmployeeService(final CreatePositionRequest request, final RequestContext context) {
        return employeeServiceClient.createPosition(request, context).getBody();
    }

    @Override
    public PositionResponse createPositionInOrganisationService(final CreatePositionRequest request, final RequestContext context) {
        return organisationServiceClient.createPosition(request, context).getBody();
    }

    @Override
    public void deletePositionInEmployeeService(final UUID positionId, final RequestContext context) {
        employeeServiceClient.deletePosition(positionId, context);
    }

    @Override
    public void deletePositionInOrganisationService(final UUID positionId, final RequestContext context) {
        organisationServiceClient.deletePosition(positionId, context);
    }
}
