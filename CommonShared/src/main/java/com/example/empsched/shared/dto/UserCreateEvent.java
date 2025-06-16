package com.example.empsched.shared.dto;

import com.example.empsched.shared.entity.Role;
import lombok.Builder;

import java.util.UUID;

@Builder
public record UserCreateEvent(UUID id, String email, String password, Role role) {
}
