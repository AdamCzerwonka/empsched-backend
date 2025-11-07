package com.example.empsched.employee.exception;

import com.example.empsched.employee.util.EmployeeErrorCodes;
import com.example.empsched.shared.exception.ApplicationException;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class AbsenceAlreadyApprovedException extends ApplicationException {
    private static final String MESSAGE_ID = "Absence with id %s is already approved";

    public AbsenceAlreadyApprovedException(final UUID absenceId) {
        super(String.format(MESSAGE_ID, absenceId), EmployeeErrorCodes.ABSENCE_ALREADY_APPROVED.getKey(), HttpStatus.CONFLICT);
    }
}
