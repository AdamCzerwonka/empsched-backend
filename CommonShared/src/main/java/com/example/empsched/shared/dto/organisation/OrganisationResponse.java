package com.example.empsched.shared.dto.organisation;

import java.util.UUID;

public record OrganisationResponse(
        UUID id,
        String name,
        int maxEmployees,
        UUID ownerId
) {
}
