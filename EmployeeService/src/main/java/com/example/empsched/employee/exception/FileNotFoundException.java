package com.example.empsched.employee.exception;

import com.example.empsched.employee.util.EmployeeErrorCodes;
import com.example.empsched.shared.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class FileNotFoundException extends ApplicationException {
    private static final String MESSAGE = "File not found: %s";

    public FileNotFoundException(final String fileName) {
        super(String.format(MESSAGE, fileName), EmployeeErrorCodes.FILE_NOT_FOUND.getKey(), HttpStatus.NOT_FOUND);
    }
}
