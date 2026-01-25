package com.example.empsched.scheduling.exceptions;

import com.example.empsched.scheduling.util.SchedulingErrorCodes;
import com.example.empsched.shared.exception.ApplicationException;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class OrganisationNotFound extends ApplicationException {
    private static final String MESSAGE = "Organisation with id '%s' not found";

    public OrganisationNotFound(final UUID id) {
        super(String.format(MESSAGE, id), SchedulingErrorCodes.ORGANISATION_NOT_FOUND.getKey(), HttpStatus.NOT_FOUND);
    }
}
