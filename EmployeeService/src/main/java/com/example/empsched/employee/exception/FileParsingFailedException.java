package com.example.empsched.employee.exception;

import com.example.empsched.employee.util.EmployeeErrorCodes;
import com.example.empsched.shared.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class FileParsingFailedException extends ApplicationException {
    private static final String MESSAGE = "Failed to parse file: %s";

    public FileParsingFailedException(final String fileName) {
        super(String.format(MESSAGE, fileName), EmployeeErrorCodes.FILE_PARSING_FAILED.getKey(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
