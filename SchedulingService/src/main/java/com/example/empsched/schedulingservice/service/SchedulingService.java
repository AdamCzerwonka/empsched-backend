package com.example.empsched.schedulingservice.service;

import ai.timefold.solver.core.api.solver.SolverManager;
import com.example.empsched.schedulingservice.entity.Schedule;
import com.example.empsched.schedulingservice.entity.ScheduleStatus;
import com.example.empsched.schedulingservice.entity.SchedulingEmployee;
import com.example.empsched.schedulingservice.entity.Shift;
import com.example.empsched.schedulingservice.repository.ScheduleRepository;
import com.example.empsched.schedulingservice.repository.SchedulingEmployeeRepository;
import com.example.empsched.schedulingservice.repository.ShiftRepository;
import com.example.empsched.schedulingservice.schedule.WorkSchedule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class SchedulingService {

    private final ShiftRepository shiftRepository;
    private final SchedulingEmployeeRepository employeeRepository;
    private final SolverManager<WorkSchedule, UUID> solverManager;
    private final ScheduleRepository scheduleRepository;

    public void solveSchedule(UUID scheduleId) {

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
                    log.error("Solving failed for schedule {}", id, exception);
                })

                // 4. EXECUTE: Returns a SolverJob immediately (Async)
                .run();
    }

    // Helper to load data (The "Problem Finder")
    private WorkSchedule loadProblem(UUID scheduleId) {
        List<SchedulingEmployee> employees = employeeRepository.findAll();
        List<Shift> shifts = shiftRepository.findAllByScheduleId(scheduleId);
        return new WorkSchedule(scheduleId, employees, shifts);
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