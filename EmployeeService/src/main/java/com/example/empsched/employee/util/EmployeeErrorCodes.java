package com.example.empsched.employee.util;

import com.example.empsched.shared.util.ErrorCode;

public enum EmployeeErrorCodes implements ErrorCode {
    ABSENCE_OVERLAP("absence.overlap"),
    EMPLOYEE_NOT_FOUND("employee.not.found"),
    EMPLOYEE_LIMIT_REACHED("employee.limit.reached"),
    ORGANISATION_NOT_FOUND("organisation.not.found"),
    POSITION_NOT_FOUND("position.not.found")
    ;

    private final String key;

    EmployeeErrorCodes(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return key;
    }
}
