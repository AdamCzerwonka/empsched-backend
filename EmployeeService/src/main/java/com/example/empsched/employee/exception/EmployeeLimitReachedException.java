package com.example.empsched.employee.exception;

import com.example.empsched.shared.exception.ApplicationException;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class EmployeeLimitReachedException extends ApplicationException {
    private static final String MESSAGE = "Employee limit reached for organisation %s and cannot add more employees. Limit is %s";

    public EmployeeLimitReachedException(final UUID organisationId, final int limit) {
        super(String.format(MESSAGE, organisationId, limit), "employee.limit.reached", HttpStatus.CONFLICT);
    }
}
