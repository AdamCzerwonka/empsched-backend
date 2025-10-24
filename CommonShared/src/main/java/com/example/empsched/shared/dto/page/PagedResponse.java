package com.example.empsched.shared.dto.page;

import java.util.List;

public record PagedResponse<T>(
    List<T> content,
    Long totalElements,
    Integer totalPages
){}
