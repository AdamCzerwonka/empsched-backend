package com.example.empsched.organisation.mapper;

import com.example.empsched.organisation.entity.Organisation;
import com.example.empsched.organisation.entity.Position;
import com.example.empsched.shared.configuration.BaseMapperConfig;
import com.example.empsched.shared.dto.organisation.CreateOrganisationRequest;
import com.example.empsched.shared.dto.organisation.OrganisationResponse;
import com.example.empsched.shared.dto.position.CreatePositionRequest;
import com.example.empsched.shared.dto.position.PositionResponse;
import org.mapstruct.Mapper;

@Mapper(config = BaseMapperConfig.class)
public interface DtoMapper {
    Organisation mapToOrganisation(CreateOrganisationRequest request);

    OrganisationResponse mapToOrganisationResponse(Organisation organisation);

    PositionResponse mapToPositionResponse(Position position);

    Position mapToPosition(CreatePositionRequest request);
}
