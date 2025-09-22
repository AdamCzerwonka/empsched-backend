package com.example.empsched.workflow.activity;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

import java.util.UUID;

@ActivityInterface
public interface DeleteActivities {
    @ActivityMethod
    void deleteUserInAuthService(final UUID id);

    @ActivityMethod
    void deleteOrganisationInEmployeeService(final UUID id);

    @ActivityMethod
    void deleteOrganisationInOrganisationService(final UUID id);
}
