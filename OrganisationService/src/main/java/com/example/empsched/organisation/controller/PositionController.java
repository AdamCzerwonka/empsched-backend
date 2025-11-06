package com.example.empsched.organisation.controller;

import com.example.empsched.organisation.entity.Position;
import com.example.empsched.organisation.mapper.DtoMapper;
import com.example.empsched.organisation.service.PositionService;
import com.example.empsched.organisation.util.WorkflowTasks;
import com.example.empsched.organisation.workflow.CreatePositionWorkflow;
import com.example.empsched.shared.dto.page.FilterRequest;
import com.example.empsched.shared.dto.page.PagedResponse;
import com.example.empsched.shared.dto.position.CreatePositionRequest;
import com.example.empsched.shared.dto.position.PositionResponse;
import com.example.empsched.shared.mapper.BaseMapper;
import com.example.empsched.shared.util.CredentialsExtractor;
import com.example.empsched.shared.util.RequestContext;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/positions")
@RequiredArgsConstructor
@Transactional(propagation = Propagation.NEVER)
public class PositionController {
    private final WorkflowClient client;
    private final PositionService positionService;
    private final DtoMapper mapper;
    private final BaseMapper baseMapper;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ORGANISATION_ADMIN')")
    public ResponseEntity<PagedResponse<PositionResponse>> getOrganisationPositions(@Valid final FilterRequest filterRequest) {
        final UUID organisationId = CredentialsExtractor.getOrganisationIdFromContext();
        final Pageable pageable = baseMapper.mapToPageable(
                filterRequest,
                Sort.by("name").descending());

        final Page<Position> positionsPage = positionService.getOrganisationPositions(organisationId, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(baseMapper.mapToPagedResponse(positionsPage, mapper::mapToPositionResponse));
    }

    @PostMapping()
    @PreAuthorize("hasAuthority('ROLE_ORGANISATION_ADMIN')")
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
    @PreAuthorize("hasAuthority('ROLE_ORGANISATION_ADMIN')")
    public ResponseEntity<Void> deletePosition(@PathVariable final UUID positionId) {
        final UUID organisationId = CredentialsExtractor.getOrganisationIdFromContext();
        positionService.deletePosition(positionId, organisationId);
        return ResponseEntity.noContent().build();
    }
}
