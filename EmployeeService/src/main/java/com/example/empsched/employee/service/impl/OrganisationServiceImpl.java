package com.example.empsched.employee.service.impl;

import com.example.empsched.employee.entity.Employee;
import com.example.empsched.employee.entity.Organisation;
import com.example.empsched.employee.repository.EmployeeRepository;
import com.example.empsched.employee.repository.OrganisationRepository;
import com.example.empsched.employee.service.OrganisationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class OrganisationServiceImpl implements OrganisationService {
    private final OrganisationRepository organisationRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public Organisation createOrganisationWithOwner(final Organisation organisation, final Employee owner) {
        final Organisation savedOrganisation = organisationRepository.save(organisation);
        owner.setOrganisation(savedOrganisation);
        employeeRepository.save(owner);

        return savedOrganisation;
    }

    @Override
    public void deleteOrganisation(final UUID id) {
        // TODO handle cascade delete or constraints (e.g., check for associated employees)
        organisationRepository.deleteById(id);
    }
}
