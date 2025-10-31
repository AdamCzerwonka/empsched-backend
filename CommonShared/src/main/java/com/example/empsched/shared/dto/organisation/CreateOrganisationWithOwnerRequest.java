package com.example.empsched.shared.dto.organisation;

import com.example.empsched.shared.entity.OrganisationPlan;
import jakarta.validation.constraints.*;

public record CreateOrganisationWithOwnerRequest(
        @NotBlank
        @Size(min = 1, max = 50)
        String firstName,

        @NotBlank
        @Size(min = 1, max = 50)
        String lastName,

        @Size(min = 5, max = 15)
        String phoneNumber,

        @Email
        @NotBlank
        String email,

        @NotBlank
        @Size(min = 8)
        String password,

        @NotBlank
        @Size(max = 100)
        String name,

        @NotNull
        OrganisationPlan plan
) {
}
