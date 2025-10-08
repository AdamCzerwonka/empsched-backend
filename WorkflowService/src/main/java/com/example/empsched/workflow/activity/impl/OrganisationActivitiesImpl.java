package com.example.empsched.workflow.activity.impl;

import com.example.empsched.shared.dto.organisation.CreateOrganisationRequest;
import com.example.empsched.shared.dto.organisation.OrganisationResponse;
import com.example.empsched.workflow.activity.OrganisationActivities;
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
@ActivityImpl(workers = {Tasks.WORKER_CREATE_ORGANISATION})
@RequiredArgsConstructor
@Slf4j
public class OrganisationActivitiesImpl implements OrganisationActivities {
    private final AuthServiceClient authServiceClient;
    private final EmployeeServiceClient employeeServiceClient;
    private final OrganisationServiceClient organisationServiceClient;

    @Override
    public OrganisationResponse createOrganisationInEmployeeService(final CreateOrganisationRequest request) {
        return employeeServiceClient.createOrganisation(request).getBody();
    }

    @Override
    public OrganisationResponse createOrganisationInOrganisationService(final CreateOrganisationRequest request) {
        return organisationServiceClient.createOrganisation(request).getBody();
    }

    @Override
    public void deleteOrganisationInEmployeeService(final UUID id) {
        employeeServiceClient.deleteOrganisation(id);
    }

    @Override
    public void deleteOrganisationInOrganisationService(final UUID id) {
        organisationServiceClient.deleteOrganisation(id);
    }
}
