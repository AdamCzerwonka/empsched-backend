package com.example.empsched.employee.exception;

import com.example.empsched.shared.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class PositionNotFoundException extends ApplicationException {
    private static final String MESSAGE = "Position with id '%s' not found";

    public PositionNotFoundException(final Object id) {
        super(String.format(MESSAGE, id), "position.not.found", HttpStatus.NOT_FOUND);
    }
}
