package com.example.empsched.scheduling.mappers;

import com.example.empsched.scheduling.entity.Employee;
import com.example.empsched.scheduling.entity.Organisation;
import com.example.empsched.scheduling.entity.Position;
import com.example.empsched.shared.configuration.BaseMapperConfig;
import com.example.empsched.shared.dto.employee.CreateEmployeeRequest;
import com.example.empsched.shared.dto.organisation.CreateOrganisationRequest;
import com.example.empsched.shared.dto.position.CreatePositionRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = BaseMapperConfig.class)
public interface RequestMapper {

    @Mapping(source = "id", target = "id")
    Employee toEntity(CreateEmployeeRequest employeeDTO);

    @Mapping(source = "id", target = "id")
    Organisation toEntity(CreateOrganisationRequest createOrganisationRequest);

    @Mapping(source = "id", target = "id")
    Position toEntity(CreatePositionRequest request);
}

