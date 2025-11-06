package com.example.empsched.shared.exception;

import com.example.empsched.shared.util.CommonErrorCodes;
import org.springframework.http.HttpStatus;

public class StartDateAfterEndDateException extends ApplicationException {
    private static final String MESSAGE = "The provided start date is after the end date";

    public StartDateAfterEndDateException() {
        super(MESSAGE, CommonErrorCodes.START_DATE_AFTER_END_DATE.getKey(), HttpStatus.BAD_REQUEST);
    }
}
