package com.example.empsched.shared.utils;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.UUID;

@Getter
@Slf4j
public class RequestContext {
    private final UUID organisationId;
    private final String authorizationHeader;

    public RequestContext() {
        String authHeader = null;
        UUID orgId = null;
        try {
            if (RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attributes) {
                authHeader = attributes.getRequest().getHeader(HttpHeaders.AUTHORIZATION);
            }
            orgId = CredentialsExtractor.getOrganisationIdFromContext();
        } catch (Exception e) {
            log.error("Error getting request context", e);
        }
        this.authorizationHeader = authHeader;
        this.organisationId = orgId;
    }
}
