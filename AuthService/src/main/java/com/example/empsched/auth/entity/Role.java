package com.example.empsched.auth.entity;

import lombok.Getter;

@Getter
public enum Role {
    ADMIN("ROLE_ADMIN");

    private final String name;

    Role(String name) {
        this.name = name;
    }
}
