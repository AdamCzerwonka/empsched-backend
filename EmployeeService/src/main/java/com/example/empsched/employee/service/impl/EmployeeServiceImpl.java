package com.example.empsched.employee.service.impl;

import com.example.empsched.employee.entity.Employee;
import com.example.empsched.employee.entity.Organisation;
import com.example.empsched.employee.exception.EmployeeLimitReachedException;
import com.example.empsched.employee.exception.OrganisationNotFoundException;
import com.example.empsched.employee.repository.EmployeeRepository;
import com.example.empsched.employee.repository.OrganisationRepository;
import com.example.empsched.employee.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final OrganisationRepository organisationRepository;

    @Override
    public List<Employee> getAllEmployees(UUID organisationId) {
        return employeeRepository.findAllByOrganisationId(organisationId);
    }

    @Override
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

    @Override
    public void deleteEmployee(UUID employeeId, UUID organisationId) {
        employeeRepository.deleteByIdAndOrganisationId(employeeId, organisationId);
    }
}
