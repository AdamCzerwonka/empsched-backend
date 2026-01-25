package com.example.empsched.scheduling.service;

import com.example.empsched.scheduling.dto.ShiftUpdateDTO;
import com.example.empsched.scheduling.entity.Employee;
import com.example.empsched.scheduling.entity.Schedule;
import com.example.empsched.scheduling.entity.Shift;
import com.example.empsched.scheduling.exceptions.EmployeeNotFoundException;
import com.example.empsched.scheduling.exceptions.ScheduleNotFound;
import com.example.empsched.scheduling.exceptions.ShiftNotFoundExceptiom;
import com.example.empsched.scheduling.repository.EmployeeRepository;
import com.example.empsched.scheduling.repository.ScheduleRepository;
import com.example.empsched.scheduling.repository.ShiftRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShiftServiceTest {

    @Mock
    private ShiftRepository shiftRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private ScheduleRepository scheduleRepository;

    @InjectMocks
    private ShiftService shiftService;

    private Shift shift;
    private Employee employee;
    private Schedule schedule;
    private UUID shiftId;
    private UUID employeeId;
    private UUID scheduleId;
    private UUID positionId;

    @BeforeEach
    void setUp() {
        shiftId = UUID.randomUUID();
        employeeId = UUID.randomUUID();
        scheduleId = UUID.randomUUID();
        positionId = UUID.randomUUID();

        employee = new Employee();
        employee.setId(employeeId);

        schedule = new Schedule();
        schedule.setId(scheduleId);

        shift = new Shift();
        shift.setId(shiftId);
        shift.setStartTime(LocalDateTime.of(2026, 1, 26, 9, 0));
        shift.setEndTime(LocalDateTime.of(2026, 1, 26, 17, 0));
        shift.setRequiredPositionId(positionId);
        shift.setSchedule(schedule);
    }

    @Test
    void testUpdateShift_AllFields() {
        // Given
        LocalDateTime newStart = LocalDateTime.of(2026, 1, 26, 10, 0);
        LocalDateTime newEnd = LocalDateTime.of(2026, 1, 26, 18, 0);
        UUID newPositionId = UUID.randomUUID();
        UUID newEmployeeId = UUID.randomUUID();

        Employee newEmployee = new Employee();
        newEmployee.setId(newEmployeeId);

        ShiftUpdateDTO updateDTO = new ShiftUpdateDTO(
                newStart,
                newEnd,
                newPositionId,
                newEmployeeId
        );

        when(shiftRepository.findById(shiftId)).thenReturn(Optional.of(shift));
        when(employeeRepository.findById(newEmployeeId)).thenReturn(Optional.of(newEmployee));
        when(shiftRepository.save(any(Shift.class))).thenReturn(shift);

        // When
        Shift result = shiftService.updateShift(shiftId, updateDTO);

        // Then
        assertNotNull(result);
        verify(shiftRepository).findById(shiftId);
        verify(employeeRepository).findById(newEmployeeId);
        verify(shiftRepository).save(argThat(s ->
                s.getStartTime().equals(newStart) &&
                s.getEndTime().equals(newEnd) &&
                s.getRequiredPositionId().equals(newPositionId) &&
                s.getAssignedEmployee().equals(newEmployee)
        ));
    }

    @Test
    void testUpdateShift_PartialUpdate() {
        // Given
        LocalDateTime newStart = LocalDateTime.of(2026, 1, 26, 10, 0);

        ShiftUpdateDTO updateDTO = new ShiftUpdateDTO(
                newStart,
                null,
                null,
                null
        );

        when(shiftRepository.findById(shiftId)).thenReturn(Optional.of(shift));
        when(shiftRepository.save(any(Shift.class))).thenReturn(shift);

        // When
        Shift result = shiftService.updateShift(shiftId, updateDTO);

        // Then
        assertNotNull(result);
        verify(shiftRepository).findById(shiftId);
        verify(employeeRepository, never()).findById(any());
        verify(shiftRepository).save(argThat(s -> s.getStartTime().equals(newStart)));
    }

    @Test
    void testUpdateShift_UnassignEmployee() {
        // Given
        shift.setAssignedEmployee(employee);

        ShiftUpdateDTO updateDTO = new ShiftUpdateDTO(
                null,
                null,
                null,
                null
        );

        when(shiftRepository.findById(shiftId)).thenReturn(Optional.of(shift));
        when(shiftRepository.save(any(Shift.class))).thenReturn(shift);

        // When
        Shift result = shiftService.updateShift(shiftId, updateDTO);

        // Then
        assertNotNull(result);
        verify(shiftRepository).findById(shiftId);
        verify(shiftRepository).save(any(Shift.class));
    }

    @Test
    void testUpdateShift_ShiftNotFound() {
        // Given
        ShiftUpdateDTO updateDTO = new ShiftUpdateDTO(
                LocalDateTime.now(),
                null,
                null,
                null
        );

        when(shiftRepository.findById(shiftId)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(ShiftNotFoundExceptiom.class, () -> {
            shiftService.updateShift(shiftId, updateDTO);
        });

        verify(shiftRepository).findById(shiftId);
        verify(shiftRepository, never()).save(any());
    }

    @Test
    void testUpdateShift_EmployeeNotFound() {
        // Given
        UUID nonExistentEmployeeId = UUID.randomUUID();

        ShiftUpdateDTO updateDTO = new ShiftUpdateDTO(
                null,
                null,
                null,
                nonExistentEmployeeId
        );

        when(shiftRepository.findById(shiftId)).thenReturn(Optional.of(shift));
        when(employeeRepository.findById(nonExistentEmployeeId)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(EmployeeNotFoundException.class, () -> {
            shiftService.updateShift(shiftId, updateDTO);
        });

        verify(shiftRepository).findById(shiftId);
        verify(employeeRepository).findById(nonExistentEmployeeId);
        verify(shiftRepository, never()).save(any());
    }

    @Test
    void testCreateManualShift_Success() {
        // Given
        LocalDateTime startTime = LocalDateTime.of(2026, 1, 26, 9, 0);
        LocalDateTime endTime = LocalDateTime.of(2026, 1, 26, 17, 0);

        ShiftUpdateDTO createDTO = new ShiftUpdateDTO(
                startTime,
                endTime,
                positionId,
                employeeId
        );

        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(schedule));
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(shiftRepository.save(any(Shift.class))).thenAnswer(invocation -> {
            Shift savedShift = invocation.getArgument(0);
            savedShift.setId(UUID.randomUUID());
            return savedShift;
        });

        // When
        Shift result = shiftService.createManualShift(scheduleId, createDTO);

        // Then
        assertNotNull(result);
        verify(scheduleRepository).findById(scheduleId);
        verify(employeeRepository).findById(employeeId);
        verify(shiftRepository).save(argThat(s ->
                s.getSchedule().equals(schedule) &&
                s.getStartTime().equals(startTime) &&
                s.getEndTime().equals(endTime) &&
                s.getRequiredPositionId().equals(positionId) &&
                s.getAssignedEmployee().equals(employee)
        ));
    }

    @Test
    void testCreateManualShift_WithoutEmployee() {
        // Given
        LocalDateTime startTime = LocalDateTime.of(2026, 1, 26, 9, 0);
        LocalDateTime endTime = LocalDateTime.of(2026, 1, 26, 17, 0);

        ShiftUpdateDTO createDTO = new ShiftUpdateDTO(
                startTime,
                endTime,
                positionId,
                null
        );

        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(schedule));
        when(shiftRepository.save(any(Shift.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Shift result = shiftService.createManualShift(scheduleId, createDTO);

        // Then
        assertNotNull(result);
        verify(scheduleRepository).findById(scheduleId);
        verify(employeeRepository, never()).findById(any());
        verify(shiftRepository).save(argThat(s -> s.getAssignedEmployee() == null));
    }

    @Test
    void testCreateManualShift_ScheduleNotFound() {
        // Given
        ShiftUpdateDTO createDTO = new ShiftUpdateDTO(
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(8),
                positionId,
                null
        );

        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(ScheduleNotFound.class, () -> {
            shiftService.createManualShift(scheduleId, createDTO);
        });

        verify(scheduleRepository).findById(scheduleId);
        verify(shiftRepository, never()).save(any());
    }

    @Test
    void testDeleteShift_Success() {
        // Given
        when(shiftRepository.existsById(shiftId)).thenReturn(true);

        // When
        shiftService.deleteShift(shiftId);

        // Then
        verify(shiftRepository).existsById(shiftId);
        verify(shiftRepository).deleteById(shiftId);
    }

    @Test
    void testDeleteShift_NotFound() {
        // Given
        when(shiftRepository.existsById(shiftId)).thenReturn(false);

        // When/Then
        assertThrows(ShiftNotFoundExceptiom.class, () -> {
            shiftService.deleteShift(shiftId);
        });

        verify(shiftRepository).existsById(shiftId);
        verify(shiftRepository, never()).deleteById(any());
    }

    @Test
    void testUnassignShift_Success() {
        // Given
        shift.setAssignedEmployee(employee);

        when(shiftRepository.findById(shiftId)).thenReturn(Optional.of(shift));
        when(shiftRepository.save(any(Shift.class))).thenReturn(shift);

        // When
        Shift result = shiftService.unassignShift(shiftId);

        // Then
        assertNotNull(result);
        verify(shiftRepository).findById(shiftId);
        verify(shiftRepository).save(argThat(s -> s.getAssignedEmployee() == null));
    }

    @Test
    void testUnassignShift_NotFound() {
        // Given
        when(shiftRepository.findById(shiftId)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(ShiftNotFoundExceptiom.class, () -> {
            shiftService.unassignShift(shiftId);
        });

        verify(shiftRepository).findById(shiftId);
        verify(shiftRepository, never()).save(any());
    }
}
