package com.example.empsched.shared.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record UserCreateEventDto(UUID id, String email, String password) {
}
