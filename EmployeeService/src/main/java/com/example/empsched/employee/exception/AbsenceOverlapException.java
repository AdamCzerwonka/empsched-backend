package com.example.empsched.employee.exception;

import com.example.empsched.shared.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class AbsenceOverlapException extends ApplicationException {
    private static final String MESSAGE = "The employee already has an absence that overlaps with the given dates";

    public AbsenceOverlapException() {
        super(MESSAGE, "absence.overlap", HttpStatus.CONFLICT);
    }
}
