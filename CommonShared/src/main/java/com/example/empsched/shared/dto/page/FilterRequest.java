package com.example.empsched.shared.dto.page;

import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class FilterRequest {
    @Size(min = 0, message = "Page number must be positive")
    private Integer pageNumber = 0;

    @Size(min = 1, max = 100, message = "Page size must be positive and not exceed 100")
    private Integer pageSize = 10;
}
