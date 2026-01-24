package com.example.empsched.scheduling.service;

import com.example.empsched.scheduling.entity.Employee;
import com.example.empsched.scheduling.entity.Organisation;
import com.example.empsched.scheduling.exceptions.NotOrganisationOwnerException;
import com.example.empsched.scheduling.repository.EmployeeRepository;
import com.example.empsched.scheduling.repository.OrganisationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class OrganisationService {

    private final OrganisationRepository organisationRepository;
    private final EmployeeRepository employeeRepository;

    public Organisation createOrganisationWithOwner(final Organisation organisation, final Employee owner) {
        final Organisation savedOrganisation = organisationRepository.save(organisation);
        owner.setOrganisation(savedOrganisation);
        owner.setMaxWeeklyHours(40); // Default max weekly hours for owner, TODO: should owner be schedulable?
        employeeRepository.save(owner);

        return savedOrganisation;
    }

    public void deleteOrganisation(final UUID id, final UUID requestingOrganisationId) {
        if (!id.equals(requestingOrganisationId)) {
            throw new NotOrganisationOwnerException();
        }
        // TODO handle cascade delete or constraints (e.g., check for associated employees)
        organisationRepository.deleteById(id);
    }
}
