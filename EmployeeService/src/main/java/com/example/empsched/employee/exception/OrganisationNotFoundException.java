package com.example.empsched.employee.exception;

import com.example.empsched.shared.exception.ApplicationException;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class OrganisationNotFoundException extends ApplicationException {
    private static final String MESSAGE = "Organisation with id '%s' not found";

    public OrganisationNotFoundException(final UUID id) {
        super(String.format(MESSAGE, id), "organisation.not.found", HttpStatus.NOT_FOUND);
    }
}
