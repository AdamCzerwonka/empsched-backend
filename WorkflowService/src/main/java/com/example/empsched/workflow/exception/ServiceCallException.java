package com.example.empsched.workflow.exception;

import com.example.empsched.shared.dto.Problem;
import com.example.empsched.shared.exception.ApplicationException;
import org.springframework.http.HttpStatusCode;

public class ServiceCallException extends ApplicationException {
    public ServiceCallException(final Problem problem, HttpStatusCode httpStatus) {
        super(problem.message(), problem.messageKey(), httpStatus);
    }

    public ServiceCallException(final String message, final String errorCode, final HttpStatusCode httpStatus) {
        super(message, errorCode, httpStatus);
    }
}
