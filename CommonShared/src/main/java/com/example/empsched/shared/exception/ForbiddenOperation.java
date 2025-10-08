package com.example.empsched.shared.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenOperation extends ApplicationException {
    private static final String MESSAGE = "Operation is forbidden";

    public ForbiddenOperation() {
        super(MESSAGE, "operation.forbidden", HttpStatus.FORBIDDEN);
    }
}
