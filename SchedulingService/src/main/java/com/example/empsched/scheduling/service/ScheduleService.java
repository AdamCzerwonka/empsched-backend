package com.example.empsched.scheduling.service;

import com.example.empsched.scheduling.dto.ScheduleGenerationRequest;
import com.example.empsched.scheduling.entity.*;
import com.example.empsched.scheduling.exceptions.OrganisationNotFound;
import com.example.empsched.scheduling.exceptions.ScheduleNotFound;
import com.example.empsched.scheduling.repository.*;
import com.example.empsched.scheduling.solver.WorkSchedule;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final ShiftGeneratorService shiftGeneratorService;
    private final OrganisationRepository organisationRepository;
    private final ShiftRepository shiftRepository;
    private final EmployeeRepository employeeRepository;
    private final EmployeeAvailabilityRepository availabilityRepository;

    public Schedule createDraftSchedule(final ScheduleGenerationRequest requestDTO,final UUID organisationId) {
        final Schedule schedule = new Schedule();
        schedule.setStartDate(requestDTO.startDate());
        schedule.setEndDate(requestDTO.endDate());
        schedule.setStatus(ScheduleStatus.DRAFT);
        schedule.setOrganisation(organisationRepository.findById(organisationId).orElseThrow(() -> new OrganisationNotFound(organisationId)));
        List<Shift> generatedShifts = shiftGeneratorService.generateShiftsFromRequest(schedule, requestDTO);
        schedule.setShiftList(generatedShifts);
        return scheduleRepository.save(schedule);
    }

    public Schedule getScheduleById(UUID scheduleId) {
        return scheduleRepository.findById(scheduleId).orElseThrow(() -> new ScheduleNotFound(scheduleId));
    }


    public void saveSolution(final WorkSchedule solution) {
        shiftRepository.saveAll(solution.getShiftList());
        Schedule schedule = scheduleRepository.findById(solution.getScheduleId()).orElseThrow( () -> new ScheduleNotFound(solution.getScheduleId()));
        schedule.setStatus(ScheduleStatus.SOLVED);
        schedule.setScore(solution.getScore().toString());
        scheduleRepository.save(schedule);
    }

    public Schedule setScheduleStatus(UUID scheduleId, ScheduleStatus status) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(() -> new ScheduleNotFound(scheduleId));
        schedule.setStatus(status);
        return scheduleRepository.save(schedule);
    }

    @Transactional(readOnly = true)
    public WorkSchedule loadProblem(final UUID scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow();
        List<Employee> employees = employeeRepository.findAllByOrganisationId(schedule.getOrganisation().getId());
        List<Shift> shifts = shiftRepository.findAllByScheduleId(scheduleId);
        List<EmployeeAvailability> availabilities = availabilityRepository
                .findAllByDateBetween(schedule.getStartDate(), schedule.getEndDate());
        return new WorkSchedule(scheduleId, employees, shifts, availabilities);
    }

    public Page<Schedule> getSchedulesForOrganisation(final UUID organisationId, final Pageable pageable) {
        return scheduleRepository.findAllByOrganisationId(organisationId, pageable);
    }
}
