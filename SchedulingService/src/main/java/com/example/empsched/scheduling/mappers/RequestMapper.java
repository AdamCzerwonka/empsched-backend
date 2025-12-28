package com.example.empsched.scheduling.mappers;

import com.example.empsched.scheduling.entity.Employee;
import com.example.empsched.scheduling.entity.Organisation;
import com.example.empsched.scheduling.entity.Position;
import com.example.empsched.shared.configuration.BaseMapperConfig;
import com.example.empsched.shared.dto.employee.CreateEmployeeRequest;
import com.example.empsched.shared.dto.organisation.CreateOrganisationRequest;
import com.example.empsched.shared.dto.position.CreatePositionRequest;
import org.mapstruct.Mapper;

@Mapper(config = BaseMapperConfig.class)
public interface RequestMapper {

    Employee toEntity(CreateEmployeeRequest employeeDTO);

    Organisation toEntity(CreateOrganisationRequest createOrganisationRequest);

    Position toEntity(CreatePositionRequest request);
}

