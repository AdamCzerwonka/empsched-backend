package com.example.empsched.auth.service;

public interface JwtService {
    String generateToken(final String email);
}
