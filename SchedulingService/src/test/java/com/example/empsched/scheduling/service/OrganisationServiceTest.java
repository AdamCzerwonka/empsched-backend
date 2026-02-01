package com.example.empsched.scheduling.service;

import com.example.empsched.scheduling.entity.Employee;
import com.example.empsched.scheduling.entity.Organisation;
import com.example.empsched.scheduling.exceptions.NotOrganisationOwnerException;
import com.example.empsched.scheduling.repository.EmployeeRepository;
import com.example.empsched.scheduling.repository.OrganisationRepository;
import com.example.empsched.shared.entity.OrganisationPlan;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrganisationServiceTest {

    @Mock
    private OrganisationRepository organisationRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private OrganisationService organisationService;

    private Organisation organisation;
    private Employee owner;
    private UUID organisationId;

    @BeforeEach
    void setUp() {
        organisationId = UUID.randomUUID();
        organisation = new Organisation(organisationId, OrganisationPlan.UNIT);

        owner = new Employee();
        owner.setId(UUID.randomUUID());
    }

    @Test
    void testCreateOrganisationWithOwner_Success() {
        // Given
        when(organisationRepository.save(any(Organisation.class))).thenReturn(organisation);
        when(employeeRepository.save(any(Employee.class))).thenReturn(owner);

        // When
        Organisation result = organisationService.createOrganisationWithOwner(organisation, owner);

        // Then
        assertNotNull(result);
        assertEquals(organisation, owner.getOrganisation());
        assertEquals(40, owner.getMaxWeeklyHours());

        verify(organisationRepository).save(organisation);
        verify(employeeRepository).save(argThat(emp ->
            emp.getOrganisation().equals(organisation) &&
            emp.getMaxWeeklyHours() == 40
        ));
    }

    @Test
    void testCreateOrganisationWithOwner_SetsDefaultMaxWeeklyHours() {
        // Given
        owner.setMaxWeeklyHours(0); // Not set initially

        when(organisationRepository.save(any(Organisation.class))).thenReturn(organisation);
        when(employeeRepository.save(any(Employee.class))).thenReturn(owner);

        // When
        organisationService.createOrganisationWithOwner(organisation, owner);

        // Then
        verify(employeeRepository).save(argThat(emp -> emp.getMaxWeeklyHours() == 40));
    }

    @Test
    void testDeleteOrganisation_Success() {
        // Given
        UUID requestingOrganisationId = organisationId;

        // When
        organisationService.deleteOrganisation(organisationId, requestingOrganisationId);

        // Then
        verify(organisationRepository).deleteById(organisationId);
    }

    @Test
    void testDeleteOrganisation_NotOwner() {
        // Given
        UUID wrongOrganisationId = UUID.randomUUID();

        // When/Then
        assertThrows(NotOrganisationOwnerException.class, () -> {
            organisationService.deleteOrganisation(organisationId, wrongOrganisationId);
        });

        verify(organisationRepository, never()).deleteById(any());
    }

    @Test
    void testDeleteOrganisation_DifferentIds() {
        // Given
        UUID differentId = UUID.randomUUID();

        // When/Then
        assertThrows(NotOrganisationOwnerException.class, () -> {
            organisationService.deleteOrganisation(organisationId, differentId);
        });

        verify(organisationRepository, never()).deleteById(any());
    }
}
