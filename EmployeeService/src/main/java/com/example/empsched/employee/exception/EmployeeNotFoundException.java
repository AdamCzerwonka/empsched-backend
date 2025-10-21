package com.example.empsched.employee.exception;

import com.example.empsched.shared.exception.ApplicationException;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class EmployeeNotFoundException extends ApplicationException {
    private static final String MESSAGE = "Employee with id '%s' not found";

    public EmployeeNotFoundException(final UUID id) {
        super(String.format(MESSAGE, id), "employee.not.found", HttpStatus.NOT_FOUND);
    }
}
