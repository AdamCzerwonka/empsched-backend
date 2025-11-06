package com.example.empsched.shared.util;

public enum CommonErrorCodes implements ErrorCode {
    FORBIDDEN_OPERATION("forbidden.operation"),
    TOKEN_PARSING_FAILED("token.parsing.failed"),
    START_DATE_AFTER_END_DATE("validation.start.date.after.end.date");

    private final String key;

    CommonErrorCodes(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return key;
    }
}
