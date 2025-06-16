package com.example.empsched.auth.dto;

import com.example.empsched.shared.entity.Role;

import java.util.List;
import java.util.UUID;

public record UserDto(UUID id, String email, List<Role> roles) {
}
