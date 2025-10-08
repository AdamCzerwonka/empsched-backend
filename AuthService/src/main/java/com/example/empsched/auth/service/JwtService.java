package com.example.empsched.auth.service;

import com.example.empsched.shared.entity.Role;

import java.util.List;
import java.util.UUID;

public interface JwtService {
    String generateToken(String email, UUID organisationId, List<Role> roles);
}
