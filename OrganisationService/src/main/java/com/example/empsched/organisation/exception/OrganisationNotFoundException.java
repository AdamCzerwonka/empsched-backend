package com.example.empsched.organisation.exception;

import com.example.empsched.shared.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class OrganisationNotFoundException extends ApplicationException {
    public OrganisationNotFoundException(String name) {
        super("Organisation with name '" + name + "' not found", "organisation.not.found", HttpStatus.NOT_FOUND);
    }
}
