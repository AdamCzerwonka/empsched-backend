package com.example.empsched.scheduling.service;

import com.example.empsched.scheduling.dto.ScheduleGenerationRequestDTO;
import com.example.empsched.scheduling.entity.Organisation;
import com.example.empsched.scheduling.entity.Schedule;
import com.example.empsched.scheduling.entity.ScheduleStatus;
import com.example.empsched.scheduling.entity.Shift;
import com.example.empsched.scheduling.repository.OrganisationRepository;
import com.example.empsched.scheduling.repository.ScheduleRepository;
import com.example.empsched.scheduling.repository.ShiftRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final ShiftGeneratorService shiftGeneratorService;
    private final OrganisationRepository organisationRepository;

    public Schedule createDraftSchedule(ScheduleGenerationRequestDTO requestDTO, UUID organisationId) {
        Schedule schedule = new Schedule();
        schedule.setStartDate(requestDTO.startDate());
        schedule.setEndDate(requestDTO.endDate());
        schedule.setStatus(ScheduleStatus.DRAFT);
        schedule.setOrganisation(organisationRepository.findById(organisationId).orElseThrow(() -> new RuntimeException("Organisation not found"))); // TODO: Custom exception

        List<Shift> generatedShifts = shiftGeneratorService.generateShiftsFromRequest(schedule, requestDTO);
        schedule.setShiftList(generatedShifts);

        return scheduleRepository.save(schedule);
    }

    public Schedule getScheduleById(UUID scheduleId) {
        return scheduleRepository.findById(scheduleId).orElseThrow(() -> new RuntimeException("Schedule not found")); // TODO: Custom exception
    }
}
