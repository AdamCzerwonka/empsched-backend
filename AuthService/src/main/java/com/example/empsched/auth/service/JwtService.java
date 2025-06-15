package com.example.empsched.auth.service;

import com.example.empsched.auth.entity.Role;

import java.util.List;

public interface JwtService {
    String generateToken(String email, List<Role> roles);
}
