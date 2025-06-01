package com.example.empsched.auth.exception;

import com.example.empsched.shared.exception.ApplicationException;

public class LoginFailedException extends ApplicationException {

    public LoginFailedException() {
        super("Login failed. Provided credentials are incorrect.", "login.failed", 401);
    }
}
