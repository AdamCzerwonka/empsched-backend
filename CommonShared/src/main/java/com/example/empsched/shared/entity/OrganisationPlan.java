package com.example.empsched.shared.entity;

public enum OrganisationPlan {
    UNIT(25),
    DEPARTMENT(100),
    DIVISION(500),
    HEADQUARTERS(1000);

    private final int maxEmployees;

    OrganisationPlan(int maxEmployees) {
        this.maxEmployees = maxEmployees;
    }
}
