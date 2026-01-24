package com.example.empsched.employee.controller;

import com.example.empsched.employee.entity.Position;
import com.example.empsched.employee.mapper.DtoMapper;
import com.example.empsched.employee.service.PositionService;
import com.example.empsched.employee.util.WorkflowTasks;
import com.example.empsched.employee.workflow.AssignEmployeePositionWorkflow;
import com.example.empsched.employee.workflow.RemoveEmployeePositionWorkflow;
import com.example.empsched.shared.dto.position.CreatePositionRequest;
import com.example.empsched.shared.dto.position.EmployeePositionChangeRequest;
import com.example.empsched.shared.dto.position.PositionResponse;
import com.example.empsched.shared.util.CredentialsExtractor;
import com.example.empsched.shared.util.RequestContext;
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

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/positions")
@RequiredArgsConstructor
@Transactional(propagation = Propagation.NEVER)
public class PositionController {
    private final PositionService positionService;
    private final DtoMapper mapper;
    private final WorkflowClient client;

    @PostMapping()
    @PreAuthorize("hasAuthority('ROLE_ORGANISATION_ADMIN')")
    public ResponseEntity<PositionResponse> createPosition(@RequestBody @Valid final CreatePositionRequest request) {
        final UUID organisationId = CredentialsExtractor.getOrganisationIdFromContext();
        final Position position = positionService.createPosition(mapper.mapToPosition(request), organisationId);

        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.mapToPositionResponse(position));
    }

    @GetMapping("/employees/{employeeId}")
    @PreAuthorize("hasAuthority('ROLE_ORGANISATION_ADMIN')")
    public ResponseEntity<List<PositionResponse>> getEmployeePositions(@PathVariable final UUID employeeId) {
        final UUID organisationId = CredentialsExtractor.getOrganisationIdFromContext();
        final List<Position> positions = positionService.getEmployeePositions(organisationId, employeeId);
        return ResponseEntity.status(HttpStatus.OK).body(positions.stream().map(mapper::mapToPositionResponse).toList());
    }

    @PostMapping("/{positionId}/employees/{employeeId}")
    @PreAuthorize("hasAuthority('ROLE_ORGANISATION_ADMIN')")
    public ResponseEntity<List<PositionResponse>> addPositionToEmployee(@PathVariable final UUID employeeId, @PathVariable final UUID positionId) {
        final WorkflowOptions options = WorkflowOptions.newBuilder()
                .setWorkflowId(UUID.randomUUID().toString())
                .setTaskQueue(WorkflowTasks.TASK_QUEUE_EMPLOYEE_POSITION_MANAGEMENT)
                .build();

        final AssignEmployeePositionWorkflow workflow = client.newWorkflowStub(AssignEmployeePositionWorkflow.class, options);
        final List<PositionResponse> positions = workflow.assign(new EmployeePositionChangeRequest(employeeId, positionId), new RequestContext());
        return ResponseEntity.status(HttpStatus.OK).body(positions);
    }

    @DeleteMapping("/{positionId}/employees/{employeeId}")
    @PreAuthorize("hasAuthority('ROLE_ORGANISATION_ADMIN')")
    public ResponseEntity<List<PositionResponse>> removePositionFromEmployee(@PathVariable final UUID employeeId, @PathVariable final UUID positionId) {
        final WorkflowOptions options = WorkflowOptions.newBuilder()
                .setWorkflowId(UUID.randomUUID().toString())
                .setTaskQueue(WorkflowTasks.TASK_QUEUE_EMPLOYEE_POSITION_MANAGEMENT)
                .build();

        final RemoveEmployeePositionWorkflow workflow = client.newWorkflowStub(RemoveEmployeePositionWorkflow.class, options);
        final List<PositionResponse> positions = workflow.remove(new EmployeePositionChangeRequest(employeeId, positionId), new RequestContext());
        return ResponseEntity.status(HttpStatus.OK).body(positions);
    }

    @DeleteMapping("/{positionId}")
    @PreAuthorize("hasAuthority('ROLE_ORGANISATION_ADMIN')")
    public ResponseEntity<Void> deletePosition(@PathVariable final UUID positionId) {
        final UUID organisationId = CredentialsExtractor.getOrganisationIdFromContext();
        positionService.deletePosition(organisationId, positionId);
        return ResponseEntity.noContent().build();
    }
}
