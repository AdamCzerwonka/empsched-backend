package com.example.empsched.scheduling.controller;

import com.example.empsched.scheduling.dto.ScheduleResponse;
import com.example.empsched.scheduling.dto.ScheduleGenerationRequest;
import com.example.empsched.scheduling.entity.Schedule;
import com.example.empsched.scheduling.mappers.DtoMapper;
import com.example.empsched.scheduling.service.ScheduleService;
import com.example.empsched.scheduling.service.ScheduleSolverService;
import com.example.empsched.shared.dto.page.FilterRequest;
import com.example.empsched.shared.dto.page.PagedResponse;
import com.example.empsched.shared.mapper.BaseMapper;
import com.example.empsched.shared.util.CredentialsExtractor;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor
@Transactional(propagation = Propagation.NEVER)
public class ScheduleController {
    private final ScheduleSolverService schedulingService;
    private final DtoMapper dtoMapper;
    private final BaseMapper baseMapper;
    private final ScheduleService scheduleService;

    @PostMapping("/draft")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ScheduleResponse> createDraftSchedule(@RequestBody final ScheduleGenerationRequest request) {
        final UUID organisationId = CredentialsExtractor.getOrganisationIdFromContext();
        final Schedule draft = scheduleService.createDraftSchedule(request, organisationId);
        return ResponseEntity.ok(dtoMapper.toDto(draft));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PagedResponse<ScheduleResponse>> getSchedules(@Valid final FilterRequest filterRequest) {
        final UUID organisationId = CredentialsExtractor.getOrganisationIdFromContext();
        final Pageable pageable = baseMapper.mapToPageable(
                filterRequest,
                Sort.by("startDate").ascending().and(Sort.by("endDate").ascending()));
        final Page<Schedule> schedulesPage = scheduleService.getSchedulesForOrganisation(organisationId, pageable);
        return ResponseEntity.ok(baseMapper.mapToPagedResponse(schedulesPage, dtoMapper::toDto));
    }

    @GetMapping("/{scheduleId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ScheduleResponse> getSchedule(@PathVariable("scheduleId") final UUID scheduleId) {
        return ResponseEntity.ok(dtoMapper.toDto(scheduleService.getScheduleById(scheduleId)));
    }

    @PostMapping("/solve/{scheduleId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> solveSchedule(@PathVariable("scheduleId") final UUID scheduleId) {
        schedulingService.solveSchedule(scheduleId);
        return ResponseEntity.ok().build();
    }

}
