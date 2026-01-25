package com.example.empsched.organisation.activity;

import com.example.empsched.organisation.entity.Position;
import com.example.empsched.shared.dto.organisation.CreateOrganisationRequest;
import com.example.empsched.shared.dto.organisation.OrganisationResponse;
import com.example.empsched.shared.dto.position.CreatePositionRequest;
import com.example.empsched.shared.dto.position.PositionResponse;
import com.example.empsched.shared.util.RequestContext;
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

    @ActivityMethod
    PositionResponse createPositionInSchedulingService(final CreatePositionRequest request, final RequestContext context);

    @ActivityMethod
    void deletePositionInSchedulingService(final UUID positionId, final RequestContext context);


}
