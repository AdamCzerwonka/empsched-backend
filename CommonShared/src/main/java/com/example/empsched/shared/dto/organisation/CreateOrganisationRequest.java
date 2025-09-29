package com.example.empsched.shared.dto.organisation;

import com.example.empsched.shared.entity.OrganisationPlan;
import lombok.Builder;

import java.util.UUID;

@Builder
public record CreateOrganisationRequest(UUID id, String name, UUID ownerId, OrganisationPlan plan) {

}
