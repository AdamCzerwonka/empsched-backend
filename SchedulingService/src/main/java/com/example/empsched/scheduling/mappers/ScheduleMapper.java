package com.example.empsched.scheduling.mappers;

import com.example.empsched.scheduling.dto.ScheduleDTO;
import com.example.empsched.scheduling.entity.Schedule;
import com.example.empsched.shared.configuration.BaseMapperConfig;
import org.mapstruct.Mapper;

@Mapper(config = BaseMapperConfig.class)
public interface ScheduleMapper {

    ScheduleDTO toDto(Schedule schedule);
}
