package com.example.empsched.shared.client;

import com.example.empsched.shared.dto.organisation.CreateOrganisationWithOwnerRequest;
import com.example.empsched.shared.dto.organisation.OrganisationResponse;
import com.example.empsched.shared.dto.position.CreatePositionRequest;
import com.example.empsched.shared.dto.position.PositionResponse;
import com.example.empsched.shared.utils.RequestContext;
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

    public ResponseEntity<OrganisationResponse> createOrganisationWithOwner(final CreateOrganisationWithOwnerRequest request, final UUID organisationId, final UUID ownerId) {
        return serviceClient.sendRequest(SERVICE_TYPE, "/organisations?organisationId=" + organisationId + "&ownerId=" + ownerId, HttpMethod.POST, request, OrganisationResponse.class);
    }

    public ResponseEntity<Void> deleteOrganisation(final UUID id) {
        return serviceClient.sendRequest(SERVICE_TYPE, "/organisations/" + id, HttpMethod.DELETE, null, Void.class);
    }

    public ResponseEntity<PositionResponse> createPosition(final CreatePositionRequest request, final RequestContext context) {
        return serviceClient.sendRequest(SERVICE_TYPE, "/positions", HttpMethod.POST, request, PositionResponse.class, context);
    }

    public ResponseEntity<Void> deletePosition(final UUID positionId, final RequestContext context) {
        return serviceClient.sendRequest(SERVICE_TYPE, "/positions/" + positionId, HttpMethod.DELETE, null, Void.class, context);
    }
}
