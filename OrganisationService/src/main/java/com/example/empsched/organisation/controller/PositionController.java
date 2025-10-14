package com.example.empsched.organisation.controller;

import com.example.empsched.organisation.entity.Position;
import com.example.empsched.organisation.mapper.DtoMapper;
import com.example.empsched.organisation.service.PositionService;
import com.example.empsched.organisation.util.WorkflowTasks;
import com.example.empsched.organisation.workflow.CreatePositionWorkflow;
import com.example.empsched.shared.dto.position.CreatePositionRequest;
import com.example.empsched.shared.dto.position.PositionResponse;
import com.example.empsched.shared.utils.CredentialsExtractor;
import com.example.empsched.shared.utils.RequestContext;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/positions")
@RequiredArgsConstructor
public class PositionController {
    private final WorkflowClient client;
    private final PositionService positionService;
    private final DtoMapper mapper;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_ORGANISATION_ADMIN')")
    public ResponseEntity<List<PositionResponse>> getOrganisationPositions() {
        final UUID organisationId = CredentialsExtractor.getOrganisationIdFromContext();
        final List<Position> positions = positionService.getOrganisationPositions(organisationId);
        return ResponseEntity.status(HttpStatus.OK).body(positions.stream().map(mapper::mapToPositionResponse).toList());
    }

    @PostMapping()
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_ORGANISATION_ADMIN')")
    public ResponseEntity<PositionResponse> createPosition(@RequestBody @Valid final CreatePositionRequest request) {
        final WorkflowOptions options = WorkflowOptions.newBuilder()
                .setWorkflowId(UUID.randomUUID().toString())
                .setTaskQueue(WorkflowTasks.TASK_QUEUE_POSITION_MANAGEMENT)
                .build();

        final CreatePositionWorkflow workflow = client.newWorkflowStub(CreatePositionWorkflow.class, options);
        final Position position = workflow.create(request, new RequestContext());

        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.mapToPositionResponse(position));
    }

    @DeleteMapping("/{positionId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_ORGANISATION_ADMIN')")
    public ResponseEntity<Void> deletePosition(@PathVariable final UUID positionId) {
        final UUID organisationId = CredentialsExtractor.getOrganisationIdFromContext();
        positionService.deletePosition(positionId, organisationId);
        return ResponseEntity.noContent().build();
    }
}
