package com.example.empsched.shared.exception;

import com.example.empsched.shared.util.CommonErrorCodes;
import org.springframework.http.HttpStatus;

public class AuthorizationTokenParsingException extends ApplicationException {
    private static final String MESSAGE = "Failed to parse authorization token";

    public AuthorizationTokenParsingException(final Throwable cause) {
        super(MESSAGE, CommonErrorCodes.TOKEN_PARSING_FAILED.getKey(), HttpStatus.INTERNAL_SERVER_ERROR, cause);
    }
}
