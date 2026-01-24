package com.example.empsched.scheduling.exceptions;

import com.example.empsched.scheduling.util.SchedulingErrorCodes;
import com.example.empsched.shared.exception.ApplicationException;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class ShiftNotFoundExceptiom extends ApplicationException {
    private static final String MESSAGE = "Shift with id %s not found";

    public ShiftNotFoundExceptiom(UUID id) {
        super(String.format(MESSAGE, id), SchedulingErrorCodes.SHIFT_NOT_FOUND.getKey(), HttpStatus.NOT_FOUND);
    }
}
