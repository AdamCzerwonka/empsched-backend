package com.example.empsched.organisation.dto;

import jakarta.validation.constraints.*;

public record CreateOrganisationRequest(
        @NotBlank
        @Size(max = 100)
        String name,
        @NotNull
        @Min(0)
        int maxEmployees,
        @Email
        @NotBlank
        String email,
        @NotBlank
        @Size(min = 8)
        String password) {
}
