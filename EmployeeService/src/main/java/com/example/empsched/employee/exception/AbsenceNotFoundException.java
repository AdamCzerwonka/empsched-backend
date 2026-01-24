package com.example.empsched.employee.exception;

import com.example.empsched.employee.util.EmployeeErrorCodes;
import com.example.empsched.shared.exception.ApplicationException;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class AbsenceNotFoundException extends ApplicationException {
    private static final String MESSAGE = "Absence with id %s does not exist.";

    public AbsenceNotFoundException(final UUID absenceId) {
        super(String.format(MESSAGE, absenceId), EmployeeErrorCodes.ABSENCE_NOT_FOUND.getKey(), HttpStatus.NOT_FOUND);
    }
}
