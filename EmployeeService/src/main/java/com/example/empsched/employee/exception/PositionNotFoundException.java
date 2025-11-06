package com.example.empsched.employee.exception;

import com.example.empsched.employee.util.EmployeeErrorCodes;
import com.example.empsched.shared.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class PositionNotFoundException extends ApplicationException {
    private static final String MESSAGE = "Position with id '%s' not found";

    public PositionNotFoundException(final Object id) {
        super(String.format(MESSAGE, id), EmployeeErrorCodes.POSITION_NOT_FOUND.getKey(), HttpStatus.NOT_FOUND);
    }
}
