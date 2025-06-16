package com.example.empsched.shared.exception;

public class MessageParsingException extends ApplicationException {
    public MessageParsingException(Throwable cause) {
        super("Failed to parse event message", "message.parsing.failed", 500, cause);
    }
}
