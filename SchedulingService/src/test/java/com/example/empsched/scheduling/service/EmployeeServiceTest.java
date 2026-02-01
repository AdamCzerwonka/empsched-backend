package com.example.empsched.scheduling.service;

import com.example.empsched.scheduling.entity.Employee;
import com.example.empsched.scheduling.entity.Organisation;
import com.example.empsched.scheduling.exceptions.EmployeeLimitReachedException;
import com.example.empsched.scheduling.exceptions.OrganisationNotFoundException;
import com.example.empsched.scheduling.repository.EmployeeRepository;
import com.example.empsched.scheduling.repository.OrganisationRepository;
import com.example.empsched.shared.entity.OrganisationPlan;
import com.example.empsched.shared.exception.ForbiddenOperation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private OrganisationRepository organisationRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private UUID organisationId;
    private Organisation organisation;
    private Employee employee;

    @BeforeEach
    void setUp() {
        organisationId = UUID.randomUUID();
        organisation = new Organisation(organisationId, OrganisationPlan.UNIT);

        employee = new Employee();
        employee.setId(UUID.randomUUID());
        employee.setPositions(new HashSet<>());
    }

    @Test
    void testGetAllEmployees() {
        // Given
        List<Employee> employees = Arrays.asList(employee, new Employee());
        when(employeeRepository.findAllByOrganisationId(organisationId)).thenReturn(employees);

        // When
        List<Employee> result = employeeService.getAllEmployees(organisationId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(employeeRepository).findAllByOrganisationId(organisationId);
    }

    @Test
    void testCreateEmployee_Success() {
        // Given
        when(organisationRepository.findById(organisationId)).thenReturn(Optional.of(organisation));
        when(employeeRepository.countByOrganisationId(organisationId)).thenReturn(5);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        // When
        Employee result = employeeService.createEmployee(employee, organisationId);

        // Then
        assertNotNull(result);
        assertEquals(organisation, employee.getOrganisation());
        assertEquals(40, employee.getMaxWeeklyHours());
        verify(organisationRepository).findById(organisationId);
        verify(employeeRepository).countByOrganisationId(organisationId);
        verify(employeeRepository).save(employee);
    }

    @Test
    void testCreateEmployee_OrganisationNotFound() {
        // Given
        when(organisationRepository.findById(organisationId)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(OrganisationNotFoundException.class, () -> {
            employeeService.createEmployee(employee, organisationId);
        });

        verify(organisationRepository).findById(organisationId);
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void testCreateEmployee_LimitReached() {
        // Given
        int maxEmployees = organisation.getPlan().getMaxEmployees();

        when(organisationRepository.findById(organisationId)).thenReturn(Optional.of(organisation));
        when(employeeRepository.countByOrganisationId(organisationId)).thenReturn(maxEmployees);

        // When/Then
        assertThrows(EmployeeLimitReachedException.class, () -> {
            employeeService.createEmployee(employee, organisationId);
        });

        verify(organisationRepository).findById(organisationId);
        verify(employeeRepository).countByOrganisationId(organisationId);
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void testDeleteEmployee_Success() {
        // Given
        UUID employeeId = employee.getId();
        employee.setOrganisation(organisation);

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

        // When
        employeeService.deleteEmployee(employeeId, organisationId);

        // Then
        verify(employeeRepository).findById(employeeId);
        verify(employeeRepository).deleteById(employeeId);
    }

    @Test
    void testDeleteEmployee_NotFound() {
        // Given
        UUID employeeId = UUID.randomUUID();
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        // When
        employeeService.deleteEmployee(employeeId, organisationId);

        // Then
        verify(employeeRepository).findById(employeeId);
        verify(employeeRepository, never()).deleteById(any());
    }

    @Test
    void testDeleteEmployee_WrongOrganisation() {
        // Given
        UUID employeeId = employee.getId();
        UUID wrongOrganisationId = UUID.randomUUID();
        Organisation wrongOrganisation = new Organisation(wrongOrganisationId, OrganisationPlan.UNIT);
        employee.setOrganisation(wrongOrganisation);

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

        // When/Then
        assertThrows(ForbiddenOperation.class, () -> {
            employeeService.deleteEmployee(employeeId, organisationId);
        });

        verify(employeeRepository).findById(employeeId);
        verify(employeeRepository, never()).deleteById(any());
    }
}
