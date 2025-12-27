package com.example.empsched.scheduling.service;

import ai.timefold.solver.core.api.solver.SolverManager;
import com.example.empsched.scheduling.entity.*;
import com.example.empsched.scheduling.repository.EmployeeAvailabilityRepository;
import com.example.empsched.scheduling.repository.ScheduleRepository;
import com.example.empsched.scheduling.repository.EmployeeRepository;
import com.example.empsched.scheduling.repository.ShiftRepository;
import com.example.empsched.scheduling.solver.WorkSchedule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ScheduleSolverService {

    private final ShiftRepository shiftRepository;
    private final EmployeeRepository employeeRepository;
    private final SolverManager<WorkSchedule, UUID> solverManager;
    private final ScheduleRepository scheduleRepository;


    private final EmployeeAvailabilityRepository availabilityRepository; // Inject this

    private WorkSchedule loadProblem(UUID scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow();

        List<Employee> employees = employeeRepository.findAll();
        List<Shift> shifts = shiftRepository.findAllByScheduleId(scheduleId);

        // Fetch availability only for the relevant date range to optimize performance
        List<EmployeeAvailability> availabilities = availabilityRepository
                .findAllByDateBetween(schedule.getStartDate(), schedule.getEndDate());

        return new WorkSchedule(scheduleId, employees, shifts, availabilities);
    }

    public void solveSchedule(UUID scheduleId) {

        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(RuntimeException::new);
        schedule.setStatus(ScheduleStatus.SOLVING);
        scheduleRepository.save(schedule);

        solverManager.solveBuilder()
                .withProblemId(scheduleId)
                // 1. INPUT: Load the problem
                .withProblemFinder(this::loadProblem)

                // 2. OUTPUT (Option A): Save EVERY improvement (High DB load, real-time UI)
                // .withBestSolutionConsumer(solution -> saveSolution(solution))

                // 2. OUTPUT (Option B): Save only the FINAL result (Recommended for backend jobs)
                .withFinalBestSolutionConsumer(this::saveSolution)

                // 3. ERROR HANDLING: (Optional but recommended)
                .withExceptionHandler((id, exception) -> {
                    Schedule scheduleWithError = scheduleRepository.findById(id).orElseThrow(RuntimeException::new);
                    scheduleWithError.setStatus(ScheduleStatus.ERROR);
                    scheduleRepository.save(scheduleWithError);
                    log.error("Solving failed for schedule {}", id, exception);
                })

                // 4. EXECUTE: Returns a SolverJob immediately (Async)
                .run();
    }

    @Transactional
    protected void saveSolution(WorkSchedule solution) {
        // Save shifts
        for (Shift shift : solution.getShiftList()) {
            shiftRepository.save(shift);
        }

        // Update Schedule status
        // Assuming you passed the Schedule ID or object into the solution to retrieve it here
        Schedule schedule = scheduleRepository.findById(solution.getScheduleId()).orElseThrow( RuntimeException::new ); // TODO: Custom Exception
        schedule.setStatus(ScheduleStatus.SOLVED);
        schedule.setScore(solution.getScore().toString());
        scheduleRepository.save(schedule);
    }
}