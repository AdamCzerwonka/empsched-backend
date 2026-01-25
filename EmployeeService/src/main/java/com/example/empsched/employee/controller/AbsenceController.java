package com.example.empsched.employee.controller;

import com.example.empsched.employee.dto.absence.AbsenceFilterParams;
import com.example.empsched.employee.dto.absence.AbsenceResponse;
import com.example.empsched.employee.dto.absence.CreateAbsenceRequest;
import com.example.empsched.employee.dto.absence.ExtendedAbsenceFilterParams;
import com.example.empsched.employee.entity.Absence;
import com.example.empsched.employee.mapper.DtoMapper;
import com.example.empsched.employee.service.AbsenceService;
import com.example.empsched.employee.util.WorkflowTasks;
import com.example.empsched.employee.workflow.ApproveAbsenceWorkflow;
import com.example.empsched.shared.dto.page.PagedResponse;
import com.example.empsched.shared.dto.scheduling.CreateAvailabilityRequest;
import com.example.empsched.shared.mapper.BaseMapper;
import com.example.empsched.shared.util.CredentialsExtractor;
import com.example.empsched.shared.util.RequestContext;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/absences")
@RequiredArgsConstructor
@Transactional(propagation = Propagation.NEVER)
@Slf4j
public class AbsenceController {
    private final AbsenceService absenceService;
    private final DtoMapper dtoMapper;
    private final BaseMapper baseMapper;
    private final WorkflowClient client;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ORGANISATION_ADMIN')")
    public ResponseEntity<PagedResponse<AbsenceResponse>> getAllAbsences(@Valid final ExtendedAbsenceFilterParams filterParams) {
        final Pageable pageable = baseMapper.mapToPageable(filterParams, Sort.by("startDate").ascending());
        final Page<Absence> absences = absenceService.getAllAbsences(
                CredentialsExtractor.getOrganisationIdFromContext(),
                filterParams.getStartFrom(),
                filterParams.getStartTo(),
                filterParams.isApproved(),
                pageable
        );
        return ResponseEntity.ok(baseMapper.mapToPagedResponse(absences, dtoMapper::mapToAbsenceResponse));
    }

    @GetMapping("/self")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PagedResponse<AbsenceResponse>> getSelfAbsences(@Valid final AbsenceFilterParams filterParams) {
        final UUID employeeId = CredentialsExtractor.getUserIdFromContext();
        final Pageable pageable = baseMapper.mapToPageable(filterParams, Sort.by("startDate").ascending());
        final Page<Absence> absences = absenceService.getAbsencesForEmployee(
                employeeId,
                filterParams.getStartFrom(),
                filterParams.getStartTo(),
                pageable
        );
        return ResponseEntity.ok(baseMapper.mapToPagedResponse(absences, dtoMapper::mapToAbsenceResponse));
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AbsenceResponse> createSelfAbsence(@RequestBody @Valid final CreateAbsenceRequest request) {
        final Absence createdAbsence = absenceService.createUnapprovedAbsence(
                dtoMapper.mapToAbsence(request),
                CredentialsExtractor.getUserIdFromContext()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoMapper.mapToAbsenceResponse(createdAbsence));
    }

    @PostMapping("/employees/{employeeId}")
    @PreAuthorize("hasAuthority('ROLE_ORGANISATION_ADMIN')")
    public ResponseEntity<AbsenceResponse> createEmployeeAbsence(@RequestBody @Valid final CreateAbsenceRequest request, @PathVariable final UUID employeeId) {
        final Absence createdAbsence = absenceService.createApprovedAbsence(
                dtoMapper.mapToAbsence(request),
                employeeId,
                CredentialsExtractor.getOrganisationIdFromContext()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoMapper.mapToAbsenceResponse(createdAbsence));
    }

    @DeleteMapping("/{absenceId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteSelfAbsence(@PathVariable final UUID absenceId) {
        final UUID employeeId = CredentialsExtractor.getUserIdFromContext();
        absenceService.deleteAbsence(absenceId, employeeId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{absenceId}/approved")
    @PreAuthorize("hasAuthority('ROLE_ORGANISATION_ADMIN')")
    public ResponseEntity<Void> approveAbsence(@PathVariable final UUID absenceId) {
        final Absence absenceToApprove = absenceService.getAbsenceById(absenceId, CredentialsExtractor.getOrganisationIdFromContext());
        final WorkflowOptions options = WorkflowOptions.newBuilder()
                .setWorkflowId(UUID.randomUUID().toString())
                .setTaskQueue(WorkflowTasks.TASK_QUEUE_ABSENCE_MANAGEMENT)
                .build();
        final ApproveAbsenceWorkflow workflow = client.newWorkflowStub(ApproveAbsenceWorkflow.class, options);
        workflow.approveAbsence(new CreateAvailabilityRequest(
                absenceToApprove.getStartDate(),
                absenceToApprove.getEndDate(),false, absenceToApprove.getEmployee().getId(), absenceToApprove.getId()
        ), new RequestContext());
        return ResponseEntity.noContent().build();
    }
}
