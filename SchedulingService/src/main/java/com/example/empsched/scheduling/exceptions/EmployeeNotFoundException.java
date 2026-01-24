package com.example.empsched.scheduling.exceptions;

import com.example.empsched.scheduling.util.SchedulingErrorCodes;
import com.example.empsched.shared.exception.ApplicationException;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class EmployeeNotFoundException extends ApplicationException {
    private static final String MESSAGE = "Employee with id '%s' not found";

    public EmployeeNotFoundException(final UUID id) {
        super(String.format(MESSAGE, id), SchedulingErrorCodes.EMPLOYEE_NOT_FOUND.getKey(), HttpStatus.NOT_FOUND);
    }
}
