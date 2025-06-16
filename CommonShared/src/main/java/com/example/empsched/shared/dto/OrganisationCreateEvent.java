package com.example.empsched.shared.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record OrganisationCreateEvent(UUID id, String name, int maxEmployees) {

}
