package com.example.empsched.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record PushSubscriptionUnregisterRequest(
        @NotBlank
        String endpoint
) {
}
