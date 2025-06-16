package com.example.empsched.auth.exception;

import com.example.empsched.shared.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class UserAlreadyExists extends ApplicationException {
    public UserAlreadyExists(String email) {
        super("User with username '" + email + "' already exists", "user.already.exists", HttpStatus.CONFLICT);
    }
}
