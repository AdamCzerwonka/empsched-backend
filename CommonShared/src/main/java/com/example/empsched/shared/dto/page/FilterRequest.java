package com.example.empsched.shared.dto.page;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class FilterRequest {
    @Min(value = 0, message = "Page number must be non-negative")
    private Integer pageNumber = 0;

    @Min(value = 1, message = "Page size must be at least 1")
    @Max(value = 100, message = "Page size must not exceed 100")
    private Integer pageSize = 10;
}
