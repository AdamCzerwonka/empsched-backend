package com.example.empsched.schedulingservice.mappers;

import com.example.empsched.schedulingservice.dto.SchedulingEmployeeDTO;
import com.example.empsched.schedulingservice.entity.SchedulingEmployee;
import com.example.empsched.shared.configuration.BaseMapperConfig;
import org.mapstruct.Mapper;

@Mapper(config = BaseMapperConfig.class)
public interface SchedulingEmployeeMapper {

    SchedulingEmployeeDTO toDto(SchedulingEmployee schedulingEmployee);
}
