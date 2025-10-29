package com.example.empsched.employee.service.impl;

import com.example.empsched.employee.entity.Absence;
import com.example.empsched.employee.entity.Employee;
import com.example.empsched.employee.exception.AbsenceOverlapException;
import com.example.empsched.employee.exception.EmployeeNotFoundException;
import com.example.empsched.employee.exception.StartDateAfterEndDateException;
import com.example.empsched.employee.repository.AbsenceRepository;
import com.example.empsched.employee.repository.EmployeeRepository;
import com.example.empsched.employee.service.AbsenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class AbsenceServiceImpl implements AbsenceService {
    private final AbsenceRepository absenceRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public Absence createUnapprovedAbsence(Absence absence, UUID employeeId) {
        checkDateValidity(absence);
        final Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId));
        checkForOverlappingAbsences(absence, employee);

        absence.setEmployee(employee);
        absence.setApproved(false);
        return absenceRepository.save(absence);
    }

    @Override
    public Absence createApprovedAbsence(Absence absence, UUID employeeId, UUID organisationId) {
        checkDateValidity(absence);
        final Employee employee = employeeRepository.findByIdAndOrganisationId(employeeId, organisationId)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId));
        checkForOverlappingAbsences(absence, employee);

        absence.setEmployee(employee);
        absence.setApproved(true);
        return absenceRepository.save(absence);
    }

    private void checkForOverlappingAbsences(Absence absence, Employee employee) {
        absenceRepository.findCollidingEmployeeAbsence(employee.getId(), absence.getStartDate(), absence.getEndDate())
                .ifPresent(existingAbsence -> {
                    throw new AbsenceOverlapException();
                });
    }

    private void checkDateValidity(Absence absence) {
        if (absence.getStartDate().isAfter(absence.getEndDate())) {
            throw new StartDateAfterEndDateException();
        }
    }
}
