package com.example.empsched.employee.service;

import com.example.empsched.employee.entity.Absence;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.UUID;

public interface AbsenceService {
    Page<Absence> getAbsencesForEmployee(final UUID employeeId, final LocalDate startFrom, final LocalDate startTo, final Pageable pageable);

    Absence createUnapprovedAbsence(final Absence absence, final UUID employeeId);

    Absence createApprovedAbsence(final Absence absence, final UUID employeeId, final UUID organisationId);
}
