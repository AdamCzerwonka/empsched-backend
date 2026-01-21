package com.example.empsched.scheduling.controller;

import com.example.empsched.scheduling.dto.ScheduleResponse;
import com.example.empsched.scheduling.dto.ScheduleGenerationRequest;
import com.example.empsched.scheduling.entity.Schedule;
import com.example.empsched.scheduling.mappers.DtoMapper;
import com.example.empsched.scheduling.service.ScheduleService;
import com.example.empsched.scheduling.service.ScheduleSolverService;
import com.example.empsched.shared.util.CredentialsExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleSolverService schedulingService;
    private final DtoMapper dtoMapper;
    private final ScheduleService scheduleService;

    @PostMapping("/draft")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ScheduleResponse> createDraftSchedule(@RequestBody ScheduleGenerationRequest request) {
        final UUID organisationId = CredentialsExtractor.getOrganisationIdFromContext();
        Schedule draft = scheduleService.createDraftSchedule(request, organisationId);
        return ResponseEntity.ok(dtoMapper.toDto(draft));
    }

    @GetMapping("/{scheduleId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ScheduleResponse> getSchedule(@PathVariable("scheduleId") UUID scheduleId) {
        return ResponseEntity.ok(dtoMapper.toDto(scheduleService.getScheduleById(scheduleId)));
    }

    @PostMapping("/solve/{scheduleId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> solveSchedule(@PathVariable("scheduleId") UUID scheduleId) {
        schedulingService.solveSchedule(scheduleId);
        return ResponseEntity.ok().build();
    }

}
