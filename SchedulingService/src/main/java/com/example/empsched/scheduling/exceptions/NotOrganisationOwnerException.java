package com.example.empsched.scheduling.exceptions;

import com.example.empsched.scheduling.util.SchedulingErrorCodes;
import com.example.empsched.shared.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class NotOrganisationOwnerException extends ApplicationException {
    private static final String MESSAGE = "Cannot delete organisation as you are not the owner.";

    public NotOrganisationOwnerException() {
        super(MESSAGE, SchedulingErrorCodes.NOT_ORGANISATION_OWNER.getKey(), HttpStatus.BAD_REQUEST);
    }
}
