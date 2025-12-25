package com.example.empsched.schedulingservice.controller;

import com.example.empsched.schedulingservice.dto.ScheduleDTO;
import com.example.empsched.schedulingservice.entity.Schedule;
import com.example.empsched.schedulingservice.entity.ScheduleStatus;
import com.example.empsched.schedulingservice.entity.Shift;
import com.example.empsched.schedulingservice.mappers.ScheduleMapper;
import com.example.empsched.schedulingservice.repository.ScheduleRepository;
import com.example.empsched.schedulingservice.service.SchedulingService;
import com.example.empsched.schedulingservice.service.ShiftGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/scheduling")
@RequiredArgsConstructor
public class SchedulingController {

    private final ScheduleRepository scheduleRepository;
    private final ShiftGeneratorService shiftGenerator;
    private final SchedulingService schedulingService;
    private final ScheduleMapper scheduleMapper;

    @PostMapping("/draft/{mondayDate}")
    public ResponseEntity<ScheduleDTO> createDraftSchedule(@PathVariable("mondayDate") LocalDate mondayDate) {
        Schedule schedule = new Schedule();
        schedule.setStartDate(mondayDate);
        schedule.setEndDate(mondayDate.plusDays(6));
        schedule.setStatus(ScheduleStatus.DRAFT);

        // Logic to generate empty shift slots for this week (e.g. from a template)
        List<Shift> emptyShifts = shiftGenerator.generateShiftsFor(schedule);
        schedule.setShiftList(emptyShifts);

        return ResponseEntity.ok(scheduleMapper.toDto(scheduleRepository.save(schedule)));
    }

    @GetMapping("/{scheduleId}")
    public ResponseEntity<ScheduleDTO> getSchedule(@PathVariable("scheduleId") UUID scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow( RuntimeException::new );
        return ResponseEntity.ok(scheduleMapper.toDto(schedule));
    }

    @PostMapping("/solve/{scheduleId}")
    public ResponseEntity<Void> solveSchedule(@PathVariable("scheduleId") UUID scheduleId) {
        // TODO: Move to service layer
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow( RuntimeException::new );
        schedule.setStatus(ScheduleStatus.SOLVING);
        scheduleRepository.save(schedule);

        schedulingService.solveSchedule(scheduleId);
        return  ResponseEntity.ok().build();
    }
}
