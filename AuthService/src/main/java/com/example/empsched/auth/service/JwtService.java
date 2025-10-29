package com.example.empsched.auth.service;

import com.example.empsched.shared.entity.Role;

import java.util.List;
import java.util.UUID;

public interface JwtService {
    String generateToken(final UUID id, final String email, final UUID organisationId, final List<Role> roles);
}
