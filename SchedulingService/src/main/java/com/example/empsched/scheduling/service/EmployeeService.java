package com.example.empsched.scheduling.service;

import com.example.empsched.scheduling.entity.Employee;
import com.example.empsched.scheduling.entity.Organisation;
import com.example.empsched.scheduling.exceptions.EmployeeLimitReachedException;
import com.example.empsched.scheduling.exceptions.OrganisationNotFoundException;
import com.example.empsched.scheduling.repository.EmployeeRepository;
import com.example.empsched.scheduling.repository.OrganisationRepository;
import com.example.empsched.shared.util.BaseThrowChecks;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final OrganisationRepository organisationRepository;

    public Page<Employee> getAllEmployees(final UUID organisationId, final Pageable pageable) {
        return employeeRepository.findAllByOrganisationId(organisationId, pageable);
    }

    public Employee createEmployee(final Employee employee, final UUID organisationId) {
        final Organisation organisation = organisationRepository.findById(organisationId)
                .orElseThrow(() -> new OrganisationNotFoundException(organisationId));

        final int maxEmployees = organisation.getPlan().getMaxEmployees();
        final int currentEmployeeCount = employeeRepository.countByOrganisationId(organisationId);

        if (currentEmployeeCount >= maxEmployees) {
            throw new EmployeeLimitReachedException(organisation.getId(), maxEmployees);
        }

        employee.setOrganisation(organisation);
        return employeeRepository.save(employee);
    }

    public void deleteEmployee(UUID employeeId, UUID organisationId) {
        Optional<Employee> employeeOpt = employeeRepository.findById(employeeId);
        if (employeeOpt.isEmpty()) {
            return;
        }
        BaseThrowChecks.throwIfNotRelated(organisationId, employeeOpt.get().getOrganisation().getId());
        employeeRepository.deleteById(employeeId);
    }
}
