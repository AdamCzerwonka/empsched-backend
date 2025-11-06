package com.example.empsched.organisation.exception;

import com.example.empsched.organisation.util.OrganisationErrorCodes;
import com.example.empsched.shared.exception.ApplicationException;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class OrganisationNotFoundException extends ApplicationException {
    private static final String MESSAGE_NAME = "Organisation with name '%s' not found";
    private static final String MESSAGE_ID = "Organisation with id '%s' not found";

    public OrganisationNotFoundException(final String name) {
        super(String.format(MESSAGE_NAME, name), OrganisationErrorCodes.ORGANISATION_NOT_FOUND.getKey(), HttpStatus.NOT_FOUND);
    }

    public OrganisationNotFoundException(final UUID id) {
        super(String.format(MESSAGE_ID, id), OrganisationErrorCodes.ORGANISATION_NOT_FOUND.getKey(), HttpStatus.NOT_FOUND);
    }
}
