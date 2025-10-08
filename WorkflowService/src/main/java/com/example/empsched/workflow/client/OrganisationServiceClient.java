package com.example.empsched.workflow.client;

import com.example.empsched.shared.dto.organisation.CreateOrganisationRequest;
import com.example.empsched.shared.dto.organisation.OrganisationResponse;
import com.example.empsched.shared.dto.position.CreatePositionRequest;
import com.example.empsched.shared.dto.position.PositionResponse;
import com.example.empsched.workflow.util.RequestContext;
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

    public ResponseEntity<PositionResponse> createPosition(final CreatePositionRequest request, final RequestContext context) {
        return serviceClient.sendRequest(SERVICE_TYPE, "/positions", HttpMethod.POST, request, PositionResponse.class, context);
    }

    public ResponseEntity<Void> deletePosition(final UUID positionId, final RequestContext context) {
        return serviceClient.sendRequest(SERVICE_TYPE, "/positions/" + positionId, HttpMethod.DELETE, null, Void.class, context);
    }
}
