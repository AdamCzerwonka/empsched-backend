package com.example.empsched.shared.dto.organisation;

import jakarta.validation.constraints.*;

public record CreateOrganisationWithOwnerRequest(
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
