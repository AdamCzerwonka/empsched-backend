package com.example.empsched.workflow.activity.impl;

import com.example.empsched.shared.dto.user.CreateUserRequest;
import com.example.empsched.shared.dto.user.UserResponse;
import com.example.empsched.workflow.activity.UserActivities;
import com.example.empsched.workflow.client.AuthServiceClient;
import com.example.empsched.workflow.client.EmployeeServiceClient;
import com.example.empsched.workflow.client.OrganisationServiceClient;
import com.example.empsched.workflow.util.Tasks;
import io.temporal.spring.boot.ActivityImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@ActivityImpl(workers = {Tasks.WORKER_CREATE_ORGANISATION, Tasks.WORKER_EMPLOYEE_MANAGEMENT})
@RequiredArgsConstructor
@Slf4j
public class UserActivitiesImpl implements UserActivities {
    private final AuthServiceClient authServiceClient;
    private final EmployeeServiceClient employeeServiceClient;
    private final OrganisationServiceClient organisationServiceClient;

    @Override
    public UserResponse createUserInAuthService(final CreateUserRequest request) {
        return authServiceClient.createUser(request).getBody();
    }

    @Override
    public void deleteUserInAuthService(final UUID id) {
        authServiceClient.deleteUser(id);
    }


}
