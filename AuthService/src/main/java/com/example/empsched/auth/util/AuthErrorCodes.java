package com.example.empsched.auth.util;

import com.example.empsched.shared.util.ErrorCode;

public enum AuthErrorCodes implements ErrorCode {
    INVALID_CREDENTIALS("invalid.credentials"),
    USER_ALREADY_EXISTS("user.already.exists")
    ;
    private final String key;

    AuthErrorCodes(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return key;
    }
}
