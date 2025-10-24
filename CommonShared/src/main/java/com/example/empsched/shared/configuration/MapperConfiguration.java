package com.example.empsched.shared.configuration;

import com.example.empsched.shared.mapper.BaseMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfiguration {
    @Bean
    public BaseMapper baseMapper() {
        return Mappers.getMapper(BaseMapper.class);
    }
}
