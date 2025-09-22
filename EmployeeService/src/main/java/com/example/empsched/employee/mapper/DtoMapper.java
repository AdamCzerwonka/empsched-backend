package com.example.empsched.employee.mapper;

import com.example.empsched.employee.entity.Organisation;
import com.example.empsched.shared.configuration.BaseMapperConfig;
import com.example.empsched.shared.dto.organisation.CreateOrganisationRequest;
import com.example.empsched.shared.dto.organisation.OrganisationResponse;
import org.mapstruct.Mapper;

@Mapper(config = BaseMapperConfig.class)
public interface DtoMapper {
    Organisation mapToOrganisation(CreateOrganisationRequest request);

    OrganisationResponse mapToOrganisationResponse(Organisation organisation);
}
