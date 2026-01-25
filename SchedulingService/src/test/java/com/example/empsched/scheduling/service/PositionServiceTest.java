package com.example.empsched.scheduling.service;

import com.example.empsched.scheduling.entity.Employee;
import com.example.empsched.scheduling.entity.Organisation;
import com.example.empsched.scheduling.entity.Position;
import com.example.empsched.scheduling.exceptions.EmployeeNotFoundException;
import com.example.empsched.scheduling.exceptions.OrganisationNotFoundException;
import com.example.empsched.scheduling.exceptions.PositionNotFoundException;
import com.example.empsched.scheduling.repository.EmployeeRepository;
import com.example.empsched.scheduling.repository.OrganisationRepository;
import com.example.empsched.scheduling.repository.PositionRepository;
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
class PositionServiceTest {

    @Mock
    private PositionRepository positionRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private OrganisationRepository organisationRepository;

    @InjectMocks
    private PositionService positionService;

    private UUID organisationId;
    private Organisation organisation;
    private Position position;
    private Employee employee;

    @BeforeEach
    void setUp() {
        organisationId = UUID.randomUUID();
        organisation = new Organisation(organisationId, OrganisationPlan.UNIT);

        position = new Position(organisation);
        position.setId(UUID.randomUUID());

        employee = new Employee();
        employee.setId(UUID.randomUUID());
        employee.setOrganisation(organisation);
        employee.setPositions(new HashSet<>());
    }

    @Test
    void testCreatePosition_Success() {
        // Given
        Position newPosition = new Position();
        newPosition.setId(UUID.randomUUID());

        when(organisationRepository.findById(organisationId)).thenReturn(Optional.of(organisation));
        when(positionRepository.save(any(Position.class))).thenReturn(newPosition);

        // When
        Position result = positionService.createPosition(newPosition, organisationId);

        // Then
        assertNotNull(result);
        assertEquals(organisation, newPosition.getOrganisation());
        verify(organisationRepository).findById(organisationId);
        verify(positionRepository).save(newPosition);
    }

    @Test
    void testCreatePosition_OrganisationNotFound() {
        // Given
        Position newPosition = new Position();
        when(organisationRepository.findById(organisationId)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(OrganisationNotFoundException.class, () -> {
            positionService.createPosition(newPosition, organisationId);
        });

        verify(organisationRepository).findById(organisationId);
        verify(positionRepository, never()).save(any());
    }

    @Test
    void testDeletePosition_Success() {
        // Given
        UUID positionId = position.getId();
        when(positionRepository.findById(positionId)).thenReturn(Optional.of(position));

        // When
        positionService.deletePosition(positionId, organisationId);

        // Then
        verify(positionRepository).findById(positionId);
        verify(positionRepository).deleteById(positionId);
    }

    @Test
    void testDeletePosition_NotFound() {
        // Given
        UUID positionId = UUID.randomUUID();
        when(positionRepository.findById(positionId)).thenReturn(Optional.empty());

        // When
        positionService.deletePosition(positionId, organisationId);

        // Then
        verify(positionRepository).findById(positionId);
        verify(positionRepository, never()).deleteById(any());
    }

    @Test
    void testDeletePosition_WrongOrganisation() {
        // Given
        UUID wrongOrganisationId = UUID.randomUUID();
        UUID positionId = position.getId();

        when(positionRepository.findById(positionId)).thenReturn(Optional.of(position));

        // When/Then
        assertThrows(ForbiddenOperation.class, () -> {
            positionService.deletePosition(positionId, wrongOrganisationId);
        });

        verify(positionRepository).findById(positionId);
        verify(positionRepository, never()).deleteById(any());
    }

    @Test
    void testGetEmployeePositions_Success() {
        // Given
        UUID employeeId = employee.getId();
        Set<Position> positions = Set.of(position);
        employee.setPositions(positions);

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

        // When
        List<Position> result = positionService.getEmployeePositions(organisationId, employeeId);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(position));
        verify(employeeRepository).findById(employeeId);
    }

    @Test
    void testGetEmployeePositions_EmployeeNotFound() {
        // Given
        UUID employeeId = UUID.randomUUID();
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(EmployeeNotFoundException.class, () -> {
            positionService.getEmployeePositions(organisationId, employeeId);
        });

        verify(employeeRepository).findById(employeeId);
    }

    @Test
    void testGetEmployeePositions_WrongOrganisation() {
        // Given
        UUID wrongOrganisationId = UUID.randomUUID();
        UUID employeeId = employee.getId();

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

        // When/Then
        assertThrows(ForbiddenOperation.class, () -> {
            positionService.getEmployeePositions(wrongOrganisationId, employeeId);
        });

        verify(employeeRepository).findById(employeeId);
    }

    @Test
    void testAddPositionToEmployee_Success() {
        // Given
        UUID employeeId = employee.getId();
        UUID positionId = position.getId();

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(positionRepository.findById(positionId)).thenReturn(Optional.of(position));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        // When
        List<Position> result = positionService.addPositionToEmployee(organisationId, employeeId, positionId);

        // Then
        assertNotNull(result);
        assertTrue(employee.getPositions().contains(position));
        verify(employeeRepository).findById(employeeId);
        verify(positionRepository).findById(positionId);
        verify(employeeRepository).save(employee);
    }

    @Test
    void testAddPositionToEmployee_EmployeeNotFound() {
        // Given
        UUID employeeId = UUID.randomUUID();
        UUID positionId = position.getId();

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(EmployeeNotFoundException.class, () -> {
            positionService.addPositionToEmployee(organisationId, employeeId, positionId);
        });

        verify(employeeRepository).findById(employeeId);
        verify(positionRepository, never()).findById(any());
    }

    @Test
    void testAddPositionToEmployee_PositionNotFound() {
        // Given
        UUID employeeId = employee.getId();
        UUID positionId = UUID.randomUUID();

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(positionRepository.findById(positionId)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(PositionNotFoundException.class, () -> {
            positionService.addPositionToEmployee(organisationId, employeeId, positionId);
        });

        verify(employeeRepository).findById(employeeId);
        verify(positionRepository).findById(positionId);
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void testAddPositionToEmployee_WrongEmployeeOrganisation() {
        // Given
        UUID wrongOrganisationId = UUID.randomUUID();
        UUID employeeId = employee.getId();
        UUID positionId = position.getId();

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

        // When/Then
        assertThrows(ForbiddenOperation.class, () -> {
            positionService.addPositionToEmployee(wrongOrganisationId, employeeId, positionId);
        });

        verify(employeeRepository).findById(employeeId);
        verify(positionRepository, never()).findById(any());
    }

    @Test
    void testAddPositionToEmployee_WrongPositionOrganisation() {
        // Given
        UUID employeeId = employee.getId();
        UUID positionId = position.getId();

        Organisation wrongOrganisation = new Organisation(UUID.randomUUID(), OrganisationPlan.UNIT);
        Position wrongPosition = new Position(wrongOrganisation);
        wrongPosition.setId(positionId);

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(positionRepository.findById(positionId)).thenReturn(Optional.of(wrongPosition));

        // When/Then
        assertThrows(ForbiddenOperation.class, () -> {
            positionService.addPositionToEmployee(organisationId, employeeId, positionId);
        });

        verify(employeeRepository).findById(employeeId);
        verify(positionRepository).findById(positionId);
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void testRemovePositionFromEmployee_Success() {
        // Given
        UUID employeeId = employee.getId();
        UUID positionId = position.getId();
        employee.getPositions().add(position);

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(positionRepository.findById(positionId)).thenReturn(Optional.of(position));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        // When
        List<Position> result = positionService.removePositionFromEmployee(organisationId, employeeId, positionId);

        // Then
        assertNotNull(result);
        assertFalse(employee.getPositions().contains(position));
        verify(employeeRepository).findById(employeeId);
        verify(positionRepository).findById(positionId);
        verify(employeeRepository).save(employee);
    }

    @Test
    void testRemovePositionFromEmployee_EmployeeNotFound() {
        // Given
        UUID employeeId = UUID.randomUUID();
        UUID positionId = position.getId();

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(IllegalArgumentException.class, () -> {
            positionService.removePositionFromEmployee(organisationId, employeeId, positionId);
        });

        verify(employeeRepository).findById(employeeId);
    }

    @Test
    void testRemovePositionFromEmployee_PositionNotInSet() {
        // Given
        UUID employeeId = employee.getId();
        UUID positionId = position.getId();
        // Position not added to employee's set

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(positionRepository.findById(positionId)).thenReturn(Optional.of(position));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        // When
        List<Position> result = positionService.removePositionFromEmployee(organisationId, employeeId, positionId);

        // Then - Should not throw error, just doesn't remove anything
        assertNotNull(result);
        verify(employeeRepository).save(employee);
    }
}
