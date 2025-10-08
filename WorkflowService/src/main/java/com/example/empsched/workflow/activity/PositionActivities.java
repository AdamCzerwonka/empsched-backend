package com.example.empsched.workflow.activity;

import com.example.empsched.shared.dto.position.CreatePositionRequest;
import com.example.empsched.shared.dto.position.PositionResponse;
import com.example.empsched.workflow.util.RequestContext;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

import java.util.UUID;

@ActivityInterface
public interface PositionActivities {
    @ActivityMethod
    PositionResponse createPositionInEmployeeService(final CreatePositionRequest request, final RequestContext context);

    @ActivityMethod
    PositionResponse createPositionInOrganisationService(final CreatePositionRequest request, final RequestContext context);

    @ActivityMethod
    void deletePositionInEmployeeService(final UUID positionId, final RequestContext context);

    @ActivityMethod
    void deletePositionInOrganisationService(final UUID positionId, final RequestContext context);
}
