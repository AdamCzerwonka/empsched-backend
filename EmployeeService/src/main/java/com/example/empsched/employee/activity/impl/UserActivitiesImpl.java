package com.example.empsched.employee.activity.impl;

import com.example.empsched.employee.activity.UserActivities;
import com.example.empsched.employee.util.WorkflowTasks;
import com.example.empsched.shared.client.AuthServiceClient;
import com.example.empsched.shared.dto.user.CreateUserRequest;
import com.example.empsched.shared.dto.user.UserResponse;
import com.example.empsched.shared.util.RequestContext;
import io.temporal.spring.boot.ActivityImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@ActivityImpl(workers = {WorkflowTasks.WORKER_EMPLOYEE_MANAGEMENT})
@RequiredArgsConstructor
@Slf4j
public class UserActivitiesImpl implements UserActivities {
    private final AuthServiceClient authServiceClient;

    @Override
    public UserResponse createUserInAuthService(final CreateUserRequest request, final RequestContext context) {
        return authServiceClient.createUser(request, context).getBody();
    }

    @Override
    public void deleteUserInAuthService(final UUID id) {
        authServiceClient.deleteUser(id);
    }
}
