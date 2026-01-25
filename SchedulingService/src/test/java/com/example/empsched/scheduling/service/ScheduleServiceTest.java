package com.example.empsched.scheduling.service;

import com.example.empsched.scheduling.dto.ScheduleGenerationRequest;
import com.example.empsched.scheduling.entity.*;
import com.example.empsched.scheduling.exceptions.OrganisationNotFound;
import com.example.empsched.scheduling.exceptions.ScheduleNotFound;
import com.example.empsched.scheduling.repository.*;
import com.example.empsched.scheduling.solver.WorkSchedule;
import ai.timefold.solver.core.api.score.buildin.hardsoft.HardSoftScore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScheduleServiceTest {

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private ShiftGeneratorService shiftGeneratorService;

    @Mock
    private OrganisationRepository organisationRepository;

    @Mock
    private ShiftRepository shiftRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeAvailabilityRepository availabilityRepository;

    @InjectMocks
    private ScheduleService scheduleService;

    private UUID organisationId;
    private Organisation organisation;
    private ScheduleGenerationRequest request;

    @BeforeEach
    void setUp() {
        organisationId = UUID.randomUUID();
        organisation = new Organisation();
        organisation.setId(organisationId);

        LocalDate startDate = LocalDate.of(2026, 1, 26);
        LocalDate endDate = LocalDate.of(2026, 2, 1);
        request = new ScheduleGenerationRequest(
                startDate,
                endDate,
                new HashMap<>(),
                null
        );
    }

    @Test
    void testCreateDraftSchedule_Success() {
        // Given
        when(organisationRepository.findById(organisationId)).thenReturn(Optional.of(organisation));

        List<Shift> generatedShifts = new ArrayList<>();
        Shift shift = new Shift();
        shift.setId(UUID.randomUUID());
        generatedShifts.add(shift);

        when(shiftGeneratorService.generateShiftsFromRequest(any(), eq(request)))
                .thenReturn(generatedShifts);

        Schedule savedSchedule = new Schedule();
        savedSchedule.setId(UUID.randomUUID());
        savedSchedule.setStatus(ScheduleStatus.DRAFT);
        when(scheduleRepository.save(any(Schedule.class))).thenReturn(savedSchedule);

        // When
        Schedule result = scheduleService.createDraftSchedule(request, organisationId);

        // Then
        assertNotNull(result);
        assertEquals(ScheduleStatus.DRAFT, result.getStatus());
        verify(organisationRepository).findById(organisationId);
        verify(shiftGeneratorService).generateShiftsFromRequest(any(), eq(request));
        verify(scheduleRepository).save(any(Schedule.class));
    }

    @Test
    void testCreateDraftSchedule_OrganisationNotFound() {
        // Given
        when(organisationRepository.findById(organisationId)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(OrganisationNotFound.class, () -> {
            scheduleService.createDraftSchedule(request, organisationId);
        });

        verify(organisationRepository).findById(organisationId);
        verify(shiftGeneratorService, never()).generateShiftsFromRequest(any(), any());
        verify(scheduleRepository, never()).save(any());
    }

    @Test
    void testGetScheduleById_Success() {
        // Given
        UUID scheduleId = UUID.randomUUID();
        Schedule schedule = new Schedule();
        schedule.setId(scheduleId);

        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(schedule));

        // When
        Schedule result = scheduleService.getScheduleById(scheduleId);

        // Then
        assertNotNull(result);
        assertEquals(scheduleId, result.getId());
        verify(scheduleRepository).findById(scheduleId);
    }

    @Test
    void testGetScheduleById_NotFound() {
        // Given
        UUID scheduleId = UUID.randomUUID();
        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(ScheduleNotFound.class, () -> {
            scheduleService.getScheduleById(scheduleId);
        });

        verify(scheduleRepository).findById(scheduleId);
    }

    @Test
    void testSaveSolution_Success() {
        // Given
        UUID scheduleId = UUID.randomUUID();
        List<Shift> shifts = new ArrayList<>();
        Shift shift = new Shift();
        shift.setId(UUID.randomUUID());
        shifts.add(shift);

        WorkSchedule solution = new WorkSchedule(
                scheduleId,
                new ArrayList<>(),
                shifts,
                new ArrayList<>()
        );
        // Set a score on the solution using reflection since there's no setter
        try {
            java.lang.reflect.Field scoreField = WorkSchedule.class.getDeclaredField("score");
            scoreField.setAccessible(true);
            scoreField.set(solution, ai.timefold.solver.core.api.score.buildin.hardsoft.HardSoftScore.of(0, -10));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Schedule schedule = new Schedule();
        schedule.setId(scheduleId);
        schedule.setStatus(ScheduleStatus.SOLVING);

        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(schedule));
        when(scheduleRepository.save(any(Schedule.class))).thenReturn(schedule);

        // When
        scheduleService.saveSolution(solution);

        // Then
        verify(shiftRepository).saveAll(shifts);
        verify(scheduleRepository).findById(scheduleId);
        verify(scheduleRepository).save(argThat(s ->
            s.getStatus() == ScheduleStatus.SOLVED &&
            s.getScore() != null
        ));
    }

    @Test
    void testSetScheduleStatus_Success() {
        // Given
        UUID scheduleId = UUID.randomUUID();
        Schedule schedule = new Schedule();
        schedule.setId(scheduleId);
        schedule.setStatus(ScheduleStatus.DRAFT);

        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(schedule));
        when(scheduleRepository.save(any(Schedule.class))).thenReturn(schedule);

        // When
        Schedule result = scheduleService.setScheduleStatus(scheduleId, ScheduleStatus.PUBLISHED);

        // Then
        assertNotNull(result);
        verify(scheduleRepository).findById(scheduleId);
        verify(scheduleRepository).save(argThat(s -> s.getStatus() == ScheduleStatus.PUBLISHED));
    }

    @Test
    void testSetScheduleStatus_NotFound() {
        // Given
        UUID scheduleId = UUID.randomUUID();
        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(ScheduleNotFound.class, () -> {
            scheduleService.setScheduleStatus(scheduleId, ScheduleStatus.PUBLISHED);
        });

        verify(scheduleRepository).findById(scheduleId);
        verify(scheduleRepository, never()).save(any());
    }

    @Test
    void testLoadProblem_Success() {
        // Given
        UUID scheduleId = UUID.randomUUID();
        LocalDate startDate = LocalDate.of(2026, 1, 26);
        LocalDate endDate = LocalDate.of(2026, 2, 1);

        Schedule schedule = new Schedule();
        schedule.setId(scheduleId);
        schedule.setStartDate(startDate);
        schedule.setEndDate(endDate);
        schedule.setOrganisation(organisation);

        List<Employee> employees = new ArrayList<>();
        Employee employee = new Employee();
        employee.setId(UUID.randomUUID());
        employees.add(employee);

        List<Shift> shifts = new ArrayList<>();
        Shift shift = new Shift();
        shift.setId(UUID.randomUUID());
        shifts.add(shift);

        List<EmployeeAvailability> availabilities = new ArrayList<>();
        EmployeeAvailability availability = new EmployeeAvailability();
        availabilities.add(availability);

        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(schedule));
        when(employeeRepository.findAllByOrganisationId(organisationId))
                .thenReturn(employees);
        when(shiftRepository.findAllByScheduleId(scheduleId)).thenReturn(shifts);
        when(availabilityRepository.findAllByDateBetween(startDate, endDate))
                .thenReturn(availabilities);

        // When
        WorkSchedule result = scheduleService.loadProblem(scheduleId);

        // Then
        assertNotNull(result);
        assertEquals(scheduleId, result.getScheduleId());
        assertEquals(employees, result.getEmployeeList());
        assertEquals(shifts, result.getShiftList());
        assertEquals(availabilities, result.getAvailabilityList());

        verify(scheduleRepository).findById(scheduleId);
        verify(employeeRepository).findAllByOrganisationId(organisationId);
        verify(shiftRepository).findAllByScheduleId(scheduleId);
        verify(availabilityRepository).findAllByDateBetween(startDate, endDate);
    }
}
