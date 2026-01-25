package com.example.empsched.auth.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.Map;

public record PushNotificationRequest(
        @NotBlank
        String userEmail,

        @NotBlank
        String type,

        @NotBlank
        String titleKey,

        @NotBlank
        String bodyKey,

        Map<String, String> bodyParams,

        String url,

        Map<String, String> data
) {
}
