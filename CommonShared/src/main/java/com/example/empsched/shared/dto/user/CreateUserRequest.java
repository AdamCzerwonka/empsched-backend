package com.example.empsched.shared.dto.user;

import com.example.empsched.shared.entity.Role;
import lombok.Builder;

import java.util.UUID;

@Builder
public record CreateUserRequest(UUID id, String email, String password, Role role) {
}
