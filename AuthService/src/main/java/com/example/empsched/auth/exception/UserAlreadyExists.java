package com.example.empsched.auth.exception;

import com.example.empsched.auth.util.AuthErrorCodes;
import com.example.empsched.shared.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class UserAlreadyExists extends ApplicationException {
    private static final String MESSAGE_EMAIL = "User with email '%s' already exists";

    public UserAlreadyExists(final String email) {
        super(String.format(MESSAGE_EMAIL, email), AuthErrorCodes.USER_ALREADY_EXISTS.getKey(), HttpStatus.CONFLICT);
    }
}
