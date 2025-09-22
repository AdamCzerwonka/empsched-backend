package com.example.empsched.shared.configuration;

import org.mapstruct.MapperConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperConfig(
        componentModel = "spring",
        unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE,
        unmappedSourcePolicy = org.mapstruct.ReportingPolicy.IGNORE)
public class BaseMapperConfig {
}
