package com.example.empsched.shared.dto.user;

import lombok.Builder;

import java.util.UUID;

@Builder
public record UserResponse(UUID id, String email) {
}
