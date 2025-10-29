package com.example.empsched.shared.utils;

import com.example.empsched.shared.exception.MessageParsingException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CredentialsExtractor {
    public static UUID getUserIdFromContext() {
        try {
            final JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            Jwt jwt = authenticationToken.getToken();
            return UUID.fromString(jwt.getSubject());
        } catch (Exception e) {
            throw new MessageParsingException(e);
        }
    }

    public static String getUserEmailFromContext() {
        try {
            final JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            Jwt jwt = authenticationToken.getToken();
            return jwt.getClaim("email");
        } catch (Exception e) {
            throw new MessageParsingException(e);
        }
    }

    public static UUID getOrganisationIdFromContext() {
        try {
            final JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            Jwt jwt = authenticationToken.getToken();
            return UUID.fromString(jwt.getClaim("organisationId"));
        } catch (Exception e) {
            throw new MessageParsingException(e);
        }
    }
}
