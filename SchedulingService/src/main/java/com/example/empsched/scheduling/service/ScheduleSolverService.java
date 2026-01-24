package com.example.empsched.scheduling.service;

import ai.timefold.solver.core.api.solver.SolverManager;
import com.example.empsched.scheduling.entity.*;
import com.example.empsched.scheduling.exceptions.ScheduleNotFound;
import com.example.empsched.scheduling.repository.EmployeeAvailabilityRepository;
import com.example.empsched.scheduling.repository.ScheduleRepository;
import com.example.empsched.scheduling.repository.EmployeeRepository;
import com.example.empsched.scheduling.repository.ShiftRepository;
import com.example.empsched.scheduling.solver.WorkSchedule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ScheduleSolverService {

    private final SolverManager<WorkSchedule, UUID> solverManager;
    private final ScheduleService scheduleService;

    public void solveSchedule(final UUID scheduleId) {
        scheduleService.setScheduleStatus(scheduleId, ScheduleStatus.SOLVING);
        solverManager.solveBuilder()
                .withProblemId(scheduleId)
                // 1. INPUT: Load the problem
                .withProblemFinder(scheduleService::loadProblem)
                // 2. OUTPUT: Save only the FINAL result
                .withFinalBestSolutionConsumer(scheduleService::saveSolution)
                // 3. ERROR HANDLING:
                .withExceptionHandler((id, exception) -> {
                    log.error("Solving failed for schedule {}", id, exception);
                    scheduleService.setScheduleStatus(scheduleId, ScheduleStatus.ERROR);
                })
                // 4. EXECUTE: Returns a SolverJob immediately (Async)
                .run();
    }


}