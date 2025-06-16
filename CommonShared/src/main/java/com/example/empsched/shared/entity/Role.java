package com.example.empsched.shared.entity;

import lombok.Getter;

@Getter
public enum Role {
    ADMIN("ROLE_ADMIN"),
    ORGANISATION_ADMIN("ROLE_ORGANISATION_ADMIN"),
    ORGANISATION_EMPLOYEE("ROLE_ORGANISATION_EMPLOYEE");

    private final String name;

    Role(String name) {
        this.name = name;
    }
}
