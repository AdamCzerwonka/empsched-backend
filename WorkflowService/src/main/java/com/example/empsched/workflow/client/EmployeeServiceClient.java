package com.example.empsched.workflow.client;

import com.example.empsched.shared.dto.organisation.CreateOrganisationRequest;
import com.example.empsched.shared.dto.organisation.OrganisationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class EmployeeServiceClient {
    private static final ServiceType SERVICE_TYPE = ServiceType.EMPLOYEE;

    private final ServiceClient serviceClient;

    public ResponseEntity<OrganisationResponse> createOrganisation(final CreateOrganisationRequest request) {
        return serviceClient.sendRequest(SERVICE_TYPE, "/organisations", HttpMethod.POST, request, OrganisationResponse.class);
    }

    public ResponseEntity<Void> deleteOrganisation(final UUID id) {
        return serviceClient.sendRequest(SERVICE_TYPE, "/organisations/" + id, HttpMethod.DELETE, null, Void.class);
    }
}
