package com.example.empsched.organisation.activity.impl;

import com.example.empsched.organisation.activity.OrganisationActivities;
import com.example.empsched.organisation.entity.Organisation;
import com.example.empsched.organisation.service.OrganisationService;
import com.example.empsched.organisation.util.WorkflowTasks;
import com.example.empsched.shared.dto.organisation.CreateOrganisationRequest;
import com.example.empsched.shared.dto.organisation.OrganisationResponse;
import com.example.empsched.shared.client.EmployeeServiceClient;
import io.temporal.spring.boot.ActivityImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@ActivityImpl(workers = {WorkflowTasks.WORKER_ORGANISATION_MANAGEMENT})
@RequiredArgsConstructor
@Slf4j
public class OrganisationActivitiesImpl implements OrganisationActivities {
    private final EmployeeServiceClient employeeServiceClient;
    private final OrganisationService organisationService;

    @Override
    public OrganisationResponse createOrganisationInEmployeeService(final CreateOrganisationRequest request) {
        return employeeServiceClient.createOrganisation(request).getBody();
    }

    @Override
    public Organisation createOrganisationInOrganisationService(final Organisation organisation) {
        return organisationService.createOrganisation(organisation);
    }

    @Override
    public void deleteOrganisationInEmployeeService(final UUID id) {
        employeeServiceClient.deleteOrganisation(id);
    }

    @Override
    public void deleteOrganisationInOrganisationService(final UUID id) {
        organisationService.deleteOrganisation(id);
    }
}
