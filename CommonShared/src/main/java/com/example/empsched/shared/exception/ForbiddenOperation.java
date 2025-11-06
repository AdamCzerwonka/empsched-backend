package com.example.empsched.shared.exception;

import com.example.empsched.shared.util.CommonErrorCodes;
import org.springframework.http.HttpStatus;

public class ForbiddenOperation extends ApplicationException {
    private static final String MESSAGE = "Operation is forbidden";

    public ForbiddenOperation() {
        super(MESSAGE, CommonErrorCodes.FORBIDDEN_OPERATION.getKey(), HttpStatus.FORBIDDEN);
    }
}
