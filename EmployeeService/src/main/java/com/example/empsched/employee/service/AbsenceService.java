package com.example.empsched.employee.service;

import com.example.empsched.employee.entity.Absence;

import java.util.UUID;

public interface AbsenceService {
    Absence createUnapprovedAbsence(final Absence absence, final UUID employeeId);

    Absence createApprovedAbsence(final Absence absence, final UUID employeeId, final UUID organisationId);
}
