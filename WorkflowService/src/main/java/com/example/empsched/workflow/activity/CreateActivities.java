package com.example.empsched.workflow.activity;

import com.example.empsched.shared.dto.organisation.CreateOrganisationRequest;
import com.example.empsched.shared.dto.organisation.OrganisationResponse;
import com.example.empsched.shared.dto.user.CreateUserRequest;
import com.example.empsched.shared.dto.user.UserResponse;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface CreateActivities {
    @ActivityMethod
    UserResponse createUserInAuthService(final CreateUserRequest request);

    @ActivityMethod
    OrganisationResponse createOrganisationInEmployeeService(final CreateOrganisationRequest request);

    @ActivityMethod
    OrganisationResponse createOrganisationInOrganisationService(final CreateOrganisationRequest request);
}
