package com.example.empsched.workflow.activity.impl;

import com.example.empsched.shared.dto.organisation.CreateOrganisationRequest;
import com.example.empsched.shared.dto.organisation.OrganisationResponse;
import com.example.empsched.shared.dto.user.CreateUserRequest;
import com.example.empsched.shared.dto.user.UserResponse;
import com.example.empsched.workflow.activity.CreateActivities;
import com.example.empsched.workflow.client.AuthServiceClient;
import com.example.empsched.workflow.client.EmployeeServiceClient;
import com.example.empsched.workflow.client.OrganisationServiceClient;
import com.example.empsched.workflow.util.Tasks;
import io.temporal.spring.boot.ActivityImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@ActivityImpl(workers = {Tasks.WORKER_CREATE_ORGANISATION})
@RequiredArgsConstructor
@Slf4j
public class CreateActivitiesImpl implements CreateActivities {
    private final AuthServiceClient authServiceClient;
    private final EmployeeServiceClient employeeServiceClient;
    private final OrganisationServiceClient organisationServiceClient;

    @Override
    public UserResponse createUserInAuthService(final CreateUserRequest request) {
        return authServiceClient.createUser(request).getBody();
    }

    @Override
    public OrganisationResponse createOrganisationInEmployeeService(final CreateOrganisationRequest request) {
        return employeeServiceClient.createOrganisation(request).getBody();
    }

    @Override
    public OrganisationResponse createOrganisationInOrganisationService(final CreateOrganisationRequest request) {
        return organisationServiceClient.createOrganisation(request).getBody();
    }
}
