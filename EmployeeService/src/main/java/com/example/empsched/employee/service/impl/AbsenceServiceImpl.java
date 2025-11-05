package com.example.empsched.employee.service.impl;

import com.example.empsched.employee.entity.Absence;
import com.example.empsched.employee.entity.Employee;
import com.example.empsched.employee.exception.AbsenceOverlapException;
import com.example.empsched.employee.exception.EmployeeNotFoundException;
import com.example.empsched.employee.repository.AbsenceRepository;
import com.example.empsched.employee.repository.EmployeeRepository;
import com.example.empsched.employee.service.AbsenceService;
import com.example.empsched.employee.util.specification.AbsenceSpecification;
import com.example.empsched.shared.utils.BaseThrowChecks;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class AbsenceServiceImpl implements AbsenceService {
    private final AbsenceRepository absenceRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public Page<Absence> getAbsencesForEmployee(final UUID employeeId, final LocalDate startFrom, final LocalDate startTo, final Pageable pageable) {
        BaseThrowChecks.checkDateValidity(startFrom, startTo);
        Specification<Absence> specification = AbsenceSpecification.filterByEmployeeIdAndOverlappingDates(employeeId, startFrom, startTo);

        return absenceRepository.findAll(specification, pageable);
    }

    @Override
    public Absence createUnapprovedAbsence(Absence absence, UUID employeeId) {
        BaseThrowChecks.checkDateValidity(absence.getStartDate(), absence.getEndDate());
        final Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId));
        checkForOverlappingAbsences(absence, employee);

        absence.setEmployee(employee);
        absence.setApproved(false);
        return absenceRepository.save(absence);
    }

    @Override
    public Absence createApprovedAbsence(Absence absence, UUID employeeId, UUID organisationId) {
        BaseThrowChecks.checkDateValidity(absence.getStartDate(), absence.getEndDate());
        final Employee employee = employeeRepository.findByIdAndOrganisationId(employeeId, organisationId)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId));
        checkForOverlappingAbsences(absence, employee);

        absence.setEmployee(employee);
        absence.setApproved(true);
        return absenceRepository.save(absence);
    }

    @Override
    public void deleteAbsence(UUID absenceId, UUID employeeId) {
        absenceRepository.deleteByIdAndEmployeeIdAndApprovedFalse(absenceId, employeeId);
    }

    private void checkForOverlappingAbsences(Absence absence, Employee employee) {
        absenceRepository.findCollidingEmployeeAbsence(employee.getId(), absence.getStartDate(), absence.getEndDate())
                .ifPresent(existingAbsence -> {
                    throw new AbsenceOverlapException();
                });
    }
}
