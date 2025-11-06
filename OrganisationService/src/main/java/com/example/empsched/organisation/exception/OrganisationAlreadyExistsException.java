package com.example.empsched.organisation.exception;

import com.example.empsched.organisation.util.OrganisationErrorCodes;
import com.example.empsched.shared.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class OrganisationAlreadyExistsException extends ApplicationException {
    private static final String MESSAGE_NAME = "Organisation with name '%s' already exists";

    public OrganisationAlreadyExistsException(final String name) {
        super(String.format(MESSAGE_NAME, name), OrganisationErrorCodes.ORGANISATION_ALREADY_EXISTS.getKey(),
                HttpStatus.CONFLICT);
    }
}
