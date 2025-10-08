package com.example.empsched.shared.dto.position;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.UUID;

@Builder
public record CreatePositionRequest(
        UUID id,

        @NotBlank
        @Size(max = 100)
        String name,

        @Size(max = 5000)
        String description) {
}
