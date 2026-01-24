package com.example.empsched.scheduling.util;

import com.example.empsched.shared.util.ErrorCode;

public enum SchedulingErrorCodes implements ErrorCode {
    EMPLOYEE_NOT_FOUND("scheduling.employee.not.found"),
    POSITION_NOT_FOUND("scheduling.position.not.found"),
    ORGANISATION_NOT_FOUND("scheduling.organisation.not.found"),
    EMPLOYEE_LIMIT_REACHED("scheduling.employee.limit.reached"),
    NOT_ORGANISATION_OWNER("scheduling.not.organisation.owner"),
    SCHEDULE_NOT_FOUND("scheduling.schedule.not.found"),
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
