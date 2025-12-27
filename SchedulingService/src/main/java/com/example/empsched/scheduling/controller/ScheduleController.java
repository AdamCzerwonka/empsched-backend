package com.example.empsched.scheduling.controller;

import com.example.empsched.scheduling.dto.ScheduleDTO;
import com.example.empsched.scheduling.dto.ScheduleGenerationRequestDTO;
import com.example.empsched.scheduling.entity.Schedule;
import com.example.empsched.scheduling.entity.ScheduleStatus;
import com.example.empsched.scheduling.entity.Shift;
import com.example.empsched.scheduling.mappers.ScheduleMapper;
import com.example.empsched.scheduling.repository.ScheduleRepository;
import com.example.empsched.scheduling.service.ScheduleService;
import com.example.empsched.scheduling.service.ScheduleSolverService;
import com.example.empsched.scheduling.service.ShiftGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleRepository scheduleRepository;
    private final ShiftGeneratorService shiftGeneratorService;
    private final ScheduleSolverService schedulingService;
    private final ScheduleMapper scheduleMapper;
    private final ScheduleService scheduleService;

    @PostMapping("/draft")
    public ResponseEntity<ScheduleDTO> createDraftSchedule(@RequestBody ScheduleGenerationRequestDTO request) {
        UUID organisationId = UUID.fromString("7123f3ec-3517-4d3e-98e2-4e98a4cd9581");
        Schedule draft = scheduleService.createDraftSchedule(request, organisationId);

        return ResponseEntity.ok(scheduleMapper.toDto(draft));
    }

    @GetMapping("/{scheduleId}")
    public ResponseEntity<ScheduleDTO> getSchedule(@PathVariable("scheduleId") UUID scheduleId) {
        return ResponseEntity.ok(scheduleMapper.toDto(scheduleService.getScheduleById(scheduleId)));
    }

    @PostMapping("/solve/{scheduleId}")
    public ResponseEntity<Void> solveSchedule(@PathVariable("scheduleId") UUID scheduleId) {
        // TODO: Move to service layer


        schedulingService.solveSchedule(scheduleId);
        return ResponseEntity.ok().build();
    }
}
