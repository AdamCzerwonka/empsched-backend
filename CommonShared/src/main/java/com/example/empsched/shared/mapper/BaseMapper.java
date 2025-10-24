package com.example.empsched.shared.mapper;

import com.example.empsched.shared.configuration.BaseMapperConfig;
import com.example.empsched.shared.dto.page.FilterRequest;
import com.example.empsched.shared.dto.page.PagedResponse;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.function.Function;

@Mapper(config = BaseMapperConfig.class)
public interface BaseMapper {
    default <T, K> PagedResponse<T> mapToPagedResponse(Page<K> page, Function<K, T> mapper) {
        return new PagedResponse<>(
                page.getContent().stream().map(mapper).toList(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    default Pageable mapToPageable(FilterRequest filterRequest, Sort sort) {
        return PageRequest.of(filterRequest.getPageNumber(), filterRequest.getPageSize(), sort);
    }
}
