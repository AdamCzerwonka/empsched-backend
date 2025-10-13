package com.example.empsched.shared.dto.employee;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateEmployeeRequest(
        UUID id,

        @NotBlank
        @Size(min = 1, max = 50)
        String firstName,

        @NotBlank
        @Size(min = 1, max = 50)
        String lastName,

        @NotBlank
        @Size(min = 5, max = 100)
        String email,

        @Size(min = 5, max = 15)
        String phoneNumber,

        @NotBlank
        @Size(min = 8, max = 64)
        String password
) {
}
