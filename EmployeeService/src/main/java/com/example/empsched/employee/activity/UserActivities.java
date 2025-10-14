package com.example.empsched.employee.activity;

import com.example.empsched.shared.dto.user.CreateUserRequest;
import com.example.empsched.shared.dto.user.UserResponse;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

import java.util.UUID;

@ActivityInterface
public interface UserActivities {
    @ActivityMethod
    UserResponse createUserInAuthService(final CreateUserRequest request);

    @ActivityMethod
    void deleteUserInAuthService(final UUID id);
}
