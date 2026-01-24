package com.example.empsched.shared.client;

import com.example.empsched.shared.dto.employee.CreateEmployeeRequest;
import com.example.empsched.shared.dto.organisation.CreateOrganisationRequest;
import com.example.empsched.shared.dto.organisation.OrganisationResponse;
import com.example.empsched.shared.dto.position.CreatePositionRequest;
import com.example.empsched.shared.dto.position.PositionResponse;
import com.example.empsched.shared.dto.scheduling.CreateAvailabilityRequest;
import com.example.empsched.shared.dto.scheduling.EmployeeAvailabilitiesResponse;
import com.example.empsched.shared.dto.scheduling.SchedulingEmployeeResponse;
import com.example.empsched.shared.util.RequestContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class SchedulingServiceClient {
    private static final ServiceType SERVICE_TYPE = ServiceType.SCHEDULING;

    private final ServiceClient serviceClient;

    public ResponseEntity<OrganisationResponse> createOrganisationWithOwner(final CreateOrganisationRequest request) {
        return serviceClient.sendRequest(SERVICE_TYPE, "/organisations", HttpMethod.POST, request, OrganisationResponse.class);
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

    public ResponseEntity<EmployeeAvailabilitiesResponse> createEmployeeAvailabilities(final CreateAvailabilityRequest request, final RequestContext context) {
        return serviceClient.sendRequest(SERVICE_TYPE, "/availabilities", HttpMethod.POST, request,
                EmployeeAvailabilitiesResponse.class
                , context);
    }

    public void deleteEmployeeAvailabilities(final UUID absenceId,
                                             final RequestContext context) {
        serviceClient.sendRequest(SERVICE_TYPE, "/availabilities/" + absenceId, HttpMethod.DELETE, null,
                EmployeeAvailabilitiesResponse.class, context);
    }

    public ResponseEntity<SchedulingEmployeeResponse> createSchedulingEmployee(final CreateEmployeeRequest employeeResponse, final RequestContext context) {
        return serviceClient.sendRequest(SERVICE_TYPE, "/employees", HttpMethod.POST, employeeResponse,
                SchedulingEmployeeResponse.class
                , context);
    }

    public ResponseEntity<Void> deleteSchedulingEmployee(final UUID employeeId, final RequestContext context) {
        return serviceClient.sendRequest(SERVICE_TYPE, "/employees/" + employeeId, HttpMethod.DELETE, null, Void.class, context);
    }

    // TODO: Assign position to employee and remove position from employee methods

}
