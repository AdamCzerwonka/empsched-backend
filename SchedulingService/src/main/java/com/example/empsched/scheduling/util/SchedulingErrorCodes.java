package com.example.empsched.scheduling.util;

import com.example.empsched.shared.util.ErrorCode;

public enum SchedulingErrorCodes implements ErrorCode {
    EMPLOYEE_NOT_FOUND("scheduling.employee.not.found"),
    POSITION_NOT_FOUND("scheduling.position.not.found"),
    ORGANISATION_NOT_FOUND("scheduling.organisation.not.found"),
    EMPLOYEE_LIMIT_REACHED("scheduling.employee.limit.reached")
    ;

    private final String key;

    SchedulingErrorCodes(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return key;
    }
}
