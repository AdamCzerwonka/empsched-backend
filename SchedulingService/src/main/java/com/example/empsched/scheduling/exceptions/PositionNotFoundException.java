package com.example.empsched.scheduling.exceptions;

import com.example.empsched.scheduling.util.SchedulingErrorCodes;
import com.example.empsched.shared.exception.ApplicationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.util.UUID;

public class PositionNotFoundException extends ApplicationException {

    private static final String MESSAGE = "Position with id %s not found.";
    private static final String MESSAGE_KEY = SchedulingErrorCodes.POSITION_NOT_FOUND.getKey();

    public PositionNotFoundException(final UUID id) {
        super(String.format(MESSAGE, id), MESSAGE_KEY, HttpStatus.NOT_FOUND);
    }
}
