package com.example.empsched.organisation.exception;

import com.example.empsched.organisation.util.OrganisationErrorCodes;
import com.example.empsched.shared.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class PositionAlreadyExistsException extends ApplicationException {
    private static final String MESSAGE = "Position with name '%s' already exists";

    public PositionAlreadyExistsException(final String name) {
        super(String.format(MESSAGE, name), OrganisationErrorCodes.POSITION_ALREADY_EXISTS.getKey(), HttpStatus.CONFLICT);
    }
}
