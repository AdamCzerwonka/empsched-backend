package com.example.empsched.shared.dto.push;

import java.util.Map;

public record PushNotificationRequest(
        String userEmail,
        String type,
        String titleKey,
        String bodyKey,
        Map<String, String> bodyParams,
        String url,
        Map<String, String> data
) {
}
