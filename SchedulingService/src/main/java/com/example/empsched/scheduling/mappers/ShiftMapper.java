package com.example.empsched.scheduling.mappers;

import com.example.empsched.scheduling.dto.ShiftDTO;
import com.example.empsched.scheduling.entity.Shift;
import com.example.empsched.shared.configuration.BaseMapperConfig;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(config = BaseMapperConfig.class)
public interface ShiftMapper {

    ShiftDTO toDto(Shift shift);

    List<ShiftDTO> toDtoList(List<Shift> shifts);
}

