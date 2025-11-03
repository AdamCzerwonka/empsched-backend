package com.example.empsched.shared.exception;

import org.springframework.http.HttpStatus;

public class StartDateAfterEndDateException extends ApplicationException {
        private static final String MESSAGE = "The provided start date is after the end date";

    public StartDateAfterEndDateException() {
        super(MESSAGE, "startDateAfterEndDate", HttpStatus.BAD_REQUEST);
    }
}
