package com.example.empsched.schedulingservice.controller;

import com.example.empsched.schedulingservice.entity.Schedule;
import com.example.empsched.schedulingservice.entity.ScheduleStatus;
import com.example.empsched.schedulingservice.entity.Shift;
import com.example.empsched.schedulingservice.repository.ScheduleRepository;
import com.example.empsched.schedulingservice.service.ShiftGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/scheduling")
@RequiredArgsConstructor
public class SchedulingController {

    private final ScheduleRepository scheduleRepository;
    private final ShiftGeneratorService shiftGenerator;

    public Schedule createDraftSchedule(LocalDate mondayDate) {
        Schedule schedule = new Schedule();
        schedule.setStartDate(mondayDate);
        schedule.setEndDate(mondayDate.plusDays(6));
        schedule.setStatus(ScheduleStatus.DRAFT);

        // Logic to generate empty shift slots for this week (e.g. from a template)
        List<Shift> emptyShifts = shiftGenerator.generateShiftsFor(schedule);
        schedule.setShiftList(emptyShifts);

        return scheduleRepository.save(schedule);
    }
}
