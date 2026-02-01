package com.example.empsched.auth.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public record PushNotificationPayload(
        String type,
        String title,
        String body,
        String url,
        Map<String, String> data
) {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public String toJson() {
        try {
            return OBJECT_MAPPER.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize push notification payload", e);
        }
    }
}
