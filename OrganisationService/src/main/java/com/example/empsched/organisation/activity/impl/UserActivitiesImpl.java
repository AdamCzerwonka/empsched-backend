package com.example.empsched.organisation.activity.impl;

import com.example.empsched.organisation.activity.UserActivities;
import com.example.empsched.organisation.util.WorkflowTasks;
import com.example.empsched.shared.dto.user.CreateUserRequest;
import com.example.empsched.shared.dto.user.UserResponse;
import com.example.empsched.shared.client.AuthServiceClient;
import io.temporal.spring.boot.ActivityImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@ActivityImpl(workers = {WorkflowTasks.WORKER_ORGANISATION_MANAGEMENT})
@RequiredArgsConstructor
public class UserActivitiesImpl implements UserActivities {
    private final AuthServiceClient authServiceClient;

    @Override
    public UserResponse createUserInAuthService(final CreateUserRequest request) {
        return authServiceClient.createUser(request).getBody();
    }

    @Override
    public void deleteUserInAuthService(final UUID id) {
        authServiceClient.deleteUser(id);
    }
}
