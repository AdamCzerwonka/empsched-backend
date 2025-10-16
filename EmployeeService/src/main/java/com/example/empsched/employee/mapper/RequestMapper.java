package com.example.empsched.employee.mapper;

import com.example.empsched.shared.configuration.BaseMapperConfig;
import com.example.empsched.shared.dto.employee.CreateEmployeeRequest;
import com.example.empsched.shared.dto.user.CreateUserRequest;
import com.example.empsched.shared.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(config = BaseMapperConfig.class)
public interface RequestMapper {
    @Mapping(source = "id", target = "id")
    CreateEmployeeRequest mapToCreateEmployeeRequest(UUID id, CreateEmployeeRequest request);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "organisationId", target = "organisationId")
    @Mapping(source = "role", target = "role")
    CreateUserRequest mapToCreateUserRequest(UUID id, UUID organisationId, Role role, CreateEmployeeRequest request);
}
