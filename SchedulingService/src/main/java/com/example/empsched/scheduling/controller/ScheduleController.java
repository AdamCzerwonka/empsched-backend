package com.example.empsched.scheduling.controller;

import com.example.empsched.scheduling.dto.ScheduleResponse;
import com.example.empsched.scheduling.dto.ScheduleGenerationRequest;
import com.example.empsched.scheduling.entity.Schedule;
import com.example.empsched.scheduling.mappers.DtoMapper;
import com.example.empsched.scheduling.service.ScheduleService;
import com.example.empsched.scheduling.service.ScheduleSolverService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ScheduleResponse> createDraftSchedule(@RequestBody ScheduleGenerationRequest request) {
        UUID organisationId = UUID.fromString("7123f3ec-3517-4d3e-98e2-4e98a4cd9581"); // TODO: Get from auth context when security is implemented
        Schedule draft = scheduleService.createDraftSchedule(request, organisationId);
        return ResponseEntity.ok(dtoMapper.toDto(draft));
    }

    @GetMapping("/{scheduleId}")
    public ResponseEntity<ScheduleResponse> getSchedule(@PathVariable("scheduleId") UUID scheduleId) {
        return ResponseEntity.ok(dtoMapper.toDto(scheduleService.getScheduleById(scheduleId)));
    }

    @PostMapping("/solve/{scheduleId}")
    public ResponseEntity<Void> solveSchedule(@PathVariable("scheduleId") UUID scheduleId) {
        schedulingService.solveSchedule(scheduleId);
        return ResponseEntity.ok().build();
    }
}
