package com.example.empsched.organisation.activity;

import com.example.empsched.organisation.entity.Position;
import com.example.empsched.shared.dto.position.CreatePositionRequest;
import com.example.empsched.shared.dto.position.PositionResponse;
import com.example.empsched.shared.utils.RequestContext;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

import java.util.UUID;

@ActivityInterface
public interface PositionActivities {
    @ActivityMethod
    PositionResponse createPositionInEmployeeService(final CreatePositionRequest request, final RequestContext context);

    @ActivityMethod
    Position createPositionInOrganisationService(final Position position, final RequestContext context);

    @ActivityMethod
    void deletePositionInEmployeeService(final UUID positionId, final RequestContext context);

    @ActivityMethod
    void deletePositionInOrganisationService(final UUID positionId, final RequestContext context);
}
