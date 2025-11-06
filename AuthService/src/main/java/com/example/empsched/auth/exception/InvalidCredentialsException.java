package com.example.empsched.auth.exception;

import com.example.empsched.auth.util.AuthErrorCodes;
import com.example.empsched.shared.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class InvalidCredentialsException extends ApplicationException {
    private static final String MESSAGE = "Login failed. Provided credentials are incorrect.";

    public InvalidCredentialsException() {
        super(MESSAGE, AuthErrorCodes.INVALID_CREDENTIALS.getKey(), HttpStatus.UNAUTHORIZED);
    }
}
