package com.example.empsched.organisation.exception;

import com.example.empsched.shared.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class OrganisationAlreadyExistsException extends ApplicationException {
    public OrganisationAlreadyExistsException(String name) {
        super("Organisation with name '" + name + "' already exists", "organisation.already.exists",
                HttpStatus.CONFLICT);
    }
}
