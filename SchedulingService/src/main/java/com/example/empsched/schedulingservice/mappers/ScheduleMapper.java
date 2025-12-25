package com.example.empsched.schedulingservice.mappers;

import com.example.empsched.schedulingservice.dto.ScheduleDTO;
import com.example.empsched.schedulingservice.entity.Schedule;
import com.example.empsched.shared.configuration.BaseMapperConfig;
import org.mapstruct.Mapper;

@Mapper(config = BaseMapperConfig.class)
public interface ScheduleMapper {

    ScheduleDTO toDto(Schedule schedule);
}
