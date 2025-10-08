package com.example.empsched.workflow.util;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Getter
@Slf4j
public class RequestContext {
    private final String authorizationHeader;

    public RequestContext() {
        String authHeader = null;
        try {
            if (RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attributes) {
                authHeader = attributes.getRequest().getHeader(HttpHeaders.AUTHORIZATION);
            }
        } catch (Exception e) {
            log.error("Error getting request context", e);
        }
        this.authorizationHeader = authHeader;
    }
}
