package com.example.empsched.organisation.controller;

import com.example.empsched.organisation.entity.Organisation;
import com.example.empsched.organisation.mapper.DtoMapper;
import com.example.empsched.organisation.util.WorkflowTasks;
import com.example.empsched.organisation.workflow.CreateOrganisationWithOwnerWorkflow;
import com.example.empsched.organisation.service.OrganisationService;
import com.example.empsched.shared.dto.organisation.CreateOrganisationWithOwnerRequest;
import com.example.empsched.shared.dto.organisation.OrganisationResponse;
import com.example.empsched.shared.utils.CredentialsExtractor;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Transactional(propagation = Propagation.NEVER)
public class OrganisationController {
    private final OrganisationService organisationService;
    private final DtoMapper mapper;
    private final WorkflowClient client;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OrganisationResponse> getOrganisation() {
        final UUID organisationId = CredentialsExtractor.getOrganisationIdFromContext();
        final Organisation organisation = organisationService.getOrganisation(organisationId);
        return ResponseEntity.ok(mapper.mapToOrganisationResponse(organisation));
    }

    @PostMapping
    public ResponseEntity<OrganisationResponse> createOrganisation(@RequestBody @Valid final CreateOrganisationWithOwnerRequest request) {
        final WorkflowOptions options = WorkflowOptions.newBuilder()
                .setWorkflowId(UUID.randomUUID().toString())
                .setTaskQueue(WorkflowTasks.TASK_QUEUE_ORGANISATION_MANAGEMENT)
                .build();

        final CreateOrganisationWithOwnerWorkflow workflow = client.newWorkflowStub(CreateOrganisationWithOwnerWorkflow.class, options);
        final Organisation organisation = workflow.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.mapToOrganisationResponse(organisation));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrganisation(@PathVariable final UUID id) {
        organisationService.deleteOrganisation(id);
        return ResponseEntity.noContent().build();
    }
}
