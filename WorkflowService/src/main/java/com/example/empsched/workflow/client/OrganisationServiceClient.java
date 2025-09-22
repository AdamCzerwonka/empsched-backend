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
public class OrganisationServiceClient {
    private static final ServiceType SERVICE_TYPE = ServiceType.ORGANISATION;

    private final ServiceClient serviceClient;

    public ResponseEntity<OrganisationResponse> createOrganisation(final CreateOrganisationRequest request) {
        return serviceClient.sendRequest(SERVICE_TYPE, "/", HttpMethod.POST, request, OrganisationResponse.class);
    }

    public ResponseEntity<Void> deleteOrganisation(final UUID id) {
        return serviceClient.sendRequest(SERVICE_TYPE, "/" + id, HttpMethod.DELETE, null, Void.class);
    }
}
