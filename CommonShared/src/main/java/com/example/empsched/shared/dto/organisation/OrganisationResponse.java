package com.example.empsched.shared.dto.organisation;

import com.example.empsched.shared.entity.OrganisationPlan;

import java.util.UUID;

public record OrganisationResponse(
        UUID id,
        String name,
        UUID ownerId,
        OrganisationPlan plan
) {
}
