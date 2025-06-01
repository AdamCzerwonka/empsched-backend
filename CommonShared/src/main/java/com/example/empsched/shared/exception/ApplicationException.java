package com.example.empsched.shared.exception;

import lombok.Getter;

@Getter
public abstract class ApplicationException extends RuntimeException {
    private final int statusCode;
    private final String messageKey;

    protected ApplicationException(String message, String messageKey, int statusCode) {
        super(message);
        this.statusCode = statusCode;
        this.messageKey = messageKey;
    }

    protected ApplicationException(String message, String messageKey, int statusCode, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
        this.messageKey = messageKey;
    }
}
