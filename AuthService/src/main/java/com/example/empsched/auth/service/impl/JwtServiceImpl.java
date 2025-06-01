package com.example.empsched.auth.service.impl;

import com.example.empsched.auth.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    private final JwtEncoder jwtEncoder;

    @Value("${jwt.expiration.time}")
    private int tokenExpirationTime;

    @Override
    public String generateToken(final String email) {
        JwtEncoderParameters params = JwtEncoderParameters.from(
                JwtClaimsSet.builder()
                        .subject(email)
                        .expiresAt(Instant.now().plusSeconds(tokenExpirationTime))
                        .build()
        );
        return jwtEncoder.encode(params).getTokenValue();
    }
}
