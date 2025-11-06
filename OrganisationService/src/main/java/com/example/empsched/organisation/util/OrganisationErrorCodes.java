package com.example.empsched.organisation.util;

import com.example.empsched.shared.util.ErrorCode;

public enum OrganisationErrorCodes implements ErrorCode {
    ORGANISATION_ALREADY_EXISTS("organisation.already.exists"),
    ORGANISATION_NOT_FOUND("organisation.not.found"),
    POSITION_ALREADY_EXISTS("position.already.exists")
    ;

    private final String key;

    OrganisationErrorCodes(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return key;
    }
}
