package com.example.empsched.shared.dto.position;

import java.util.UUID;

public record PositionResponse(UUID id, String name, String description, UUID organisationId) {
}
