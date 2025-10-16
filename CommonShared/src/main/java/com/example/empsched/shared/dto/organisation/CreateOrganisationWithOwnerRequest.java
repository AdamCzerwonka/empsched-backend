package com.example.empsched.shared.dto.organisation;

import jakarta.validation.constraints.*;

public record CreateOrganisationWithOwnerRequest(
        @NotBlank
        @Size(max = 100)
        String name,

        @Email
        @NotBlank
        String email,

        @NotBlank
        @Size(min = 8)
        String password,

        @NotNull
        OrganisationPlan plan
) {
}
