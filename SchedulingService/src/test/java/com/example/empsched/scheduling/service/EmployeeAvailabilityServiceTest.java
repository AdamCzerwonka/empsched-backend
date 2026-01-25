package com.example.empsched.scheduling.service;

import com.example.empsched.scheduling.entity.AvailabilityType;
import com.example.empsched.scheduling.entity.Employee;
import com.example.empsched.scheduling.entity.EmployeeAvailability;
import com.example.empsched.scheduling.repository.EmployeeAvailabilityRepository;
import com.example.empsched.scheduling.repository.EmployeeRepository;
import com.example.empsched.shared.dto.scheduling.CreateAvailabilityRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeAvailabilityServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeAvailabilityRepository employeeAvailabilityRepository;

    @InjectMocks
    private EmployeeAvailabilityService employeeAvailabilityService;

    @Captor
    private ArgumentCaptor<List<EmployeeAvailability>> availabilityListCaptor;

    private UUID employeeId;
    private Employee employee;

    @BeforeEach
    void setUp() {
        employeeId = UUID.randomUUID();
        employee = new Employee();
        employee.setId(employeeId);
    }

    @Test
    void testGetEmployeeAvailability() {
        // Given
        List<EmployeeAvailability> availabilities = Arrays.asList(
                new EmployeeAvailability(),
                new EmployeeAvailability()
        );
        when(employeeAvailabilityRepository.findAllByEmployeeId(employeeId))
                .thenReturn(availabilities);

        // When
        List<EmployeeAvailability> result = employeeAvailabilityService.getEmployeeAvailability(employeeId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(employeeAvailabilityRepository).findAllByEmployeeId(employeeId);
    }

    @Test
    void testCreateEmployeeAvailabilities_Available() {
        // Given
        LocalDate startDate = LocalDate.of(2026, 1, 26);
        LocalDate endDate = LocalDate.of(2026, 1, 28);
        UUID absenceId = UUID.randomUUID();

        CreateAvailabilityRequest request = new CreateAvailabilityRequest(
                startDate,
                endDate,
                true,
                employeeId,
                absenceId
        );

        when(employeeRepository.getReferenceById(employeeId)).thenReturn(employee);
        when(employeeAvailabilityRepository.saveAll(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        List<EmployeeAvailability> result = employeeAvailabilityService.createEmployeeAvailabilities(request);

        // Then
        assertNotNull(result);
        assertEquals(3, result.size()); // 3 days

        verify(employeeRepository, times(3)).getReferenceById(employeeId);
        verify(employeeAvailabilityRepository).saveAll(availabilityListCaptor.capture());

        List<EmployeeAvailability> capturedList = availabilityListCaptor.getValue();
        assertEquals(3, capturedList.size());
        
        // Verify all are DESIRED type
        capturedList.forEach(availability -> {
            assertEquals(AvailabilityType.DESIRED, availability.getType());
            assertEquals(employee, availability.getEmployee());
            assertEquals(absenceId, availability.getAbsenceId());
        });

        // Verify dates
        assertEquals(startDate, capturedList.get(0).getDate());
        assertEquals(startDate.plusDays(1), capturedList.get(1).getDate());
        assertEquals(startDate.plusDays(2), capturedList.get(2).getDate());
    }

    @Test
    void testCreateEmployeeAvailabilities_Unavailable() {
        // Given
        LocalDate startDate = LocalDate.of(2026, 1, 26);
        LocalDate endDate = LocalDate.of(2026, 1, 27);
        UUID absenceId = UUID.randomUUID();

        CreateAvailabilityRequest request = new CreateAvailabilityRequest(
                startDate,
                endDate,
                false,
                employeeId,
                absenceId
        );

        when(employeeRepository.getReferenceById(employeeId)).thenReturn(employee);
        when(employeeAvailabilityRepository.saveAll(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        List<EmployeeAvailability> result = employeeAvailabilityService.createEmployeeAvailabilities(request);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size()); // 2 days

        verify(employeeAvailabilityRepository).saveAll(availabilityListCaptor.capture());

        List<EmployeeAvailability> capturedList = availabilityListCaptor.getValue();
        
        // Verify all are UNAVAILABLE type
        capturedList.forEach(availability -> {
            assertEquals(AvailabilityType.UNAVAILABLE, availability.getType());
            assertEquals(employee, availability.getEmployee());
            assertEquals(absenceId, availability.getAbsenceId());
        });
    }

    @Test
    void testCreateEmployeeAvailabilities_SingleDay() {
        // Given
        LocalDate date = LocalDate.of(2026, 1, 26);

        CreateAvailabilityRequest request = new CreateAvailabilityRequest(
                date,
                date,
                true,
                employeeId,
                null
        );

        when(employeeRepository.getReferenceById(employeeId)).thenReturn(employee);
        when(employeeAvailabilityRepository.saveAll(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        List<EmployeeAvailability> result = employeeAvailabilityService.createEmployeeAvailabilities(request);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(date, result.get(0).getDate());
    }

    @Test
    void testCreateEmployeeAvailabilities_WithoutAbsenceId() {
        // Given
        LocalDate startDate = LocalDate.of(2026, 1, 26);
        LocalDate endDate = LocalDate.of(2026, 1, 26);

        CreateAvailabilityRequest request = new CreateAvailabilityRequest(
                startDate,
                endDate,
                true,
                employeeId,
                null
        );

        when(employeeRepository.getReferenceById(employeeId)).thenReturn(employee);
        when(employeeAvailabilityRepository.saveAll(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        List<EmployeeAvailability> result = employeeAvailabilityService.createEmployeeAvailabilities(request);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertNull(result.get(0).getAbsenceId());
    }

    @Test
    void testDeleteEmployeeUnavailability() {
        // Given
        UUID absenceId = UUID.randomUUID();

        // When
        employeeAvailabilityService.deleteEmployeeUnavailability(absenceId);

        // Then
        verify(employeeAvailabilityRepository).deleteByAbsenceId(absenceId);
    }

    @Test
    void testCreateEmployeeAvailabilities_LongDateRange() {
        // Given
        LocalDate startDate = LocalDate.of(2026, 1, 1);
        LocalDate endDate = LocalDate.of(2026, 1, 31); // 31 days

        CreateAvailabilityRequest request = new CreateAvailabilityRequest(
                startDate,
                endDate,
                false,
                employeeId,
                UUID.randomUUID()
        );

        when(employeeRepository.getReferenceById(employeeId)).thenReturn(employee);
        when(employeeAvailabilityRepository.saveAll(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        List<EmployeeAvailability> result = employeeAvailabilityService.createEmployeeAvailabilities(request);

        // Then
        assertNotNull(result);
        assertEquals(31, result.size());
        
        verify(employeeAvailabilityRepository).saveAll(availabilityListCaptor.capture());
        
        List<EmployeeAvailability> capturedList = availabilityListCaptor.getValue();
        assertEquals(31, capturedList.size());
        
        // Verify dates are sequential
        LocalDate expectedDate = startDate;
        for (EmployeeAvailability availability : capturedList) {
            assertEquals(expectedDate, availability.getDate());
            expectedDate = expectedDate.plusDays(1);
        }
    }
}
