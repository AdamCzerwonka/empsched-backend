package com.example.empsched.auth.service.impl;

import com.example.empsched.shared.entity.Role;
import com.example.empsched.auth.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    private final JwtEncoder jwtEncoder;

    @Value("${jwt.expiration.time}")
    private int tokenExpirationTime;

    @Override
    public String generateToken(final String email, final UUID organisationId, final List<Role> roles) {
        JwtEncoderParameters params = JwtEncoderParameters.from(
                JwtClaimsSet.builder()
                        .subject(email)
                        .expiresAt(Instant.now().plusSeconds(tokenExpirationTime))
                        .claim("roles", roles.stream().map(Role::getName).toList())
                        .claim("organisationId", organisationId)
                        .build()
        );
        return jwtEncoder.encode(params).getTokenValue();
    }
}
