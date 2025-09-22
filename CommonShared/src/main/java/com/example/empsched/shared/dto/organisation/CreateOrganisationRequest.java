package com.example.empsched.shared.dto.organisation;

import lombok.Builder;

import java.util.UUID;

@Builder
public record CreateOrganisationRequest(UUID id, String name, int maxEmployees, UUID ownerId) {

}
