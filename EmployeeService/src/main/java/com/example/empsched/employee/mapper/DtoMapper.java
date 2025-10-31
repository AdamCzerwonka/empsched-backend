package com.example.empsched.employee.mapper;

import com.example.empsched.employee.dto.absence.AbsenceResponse;
import com.example.empsched.employee.dto.absence.CreateAbsenceRequest;
import com.example.empsched.employee.entity.Absence;
import com.example.empsched.employee.entity.Employee;
import com.example.empsched.employee.entity.Organisation;
import com.example.empsched.employee.entity.Position;
import com.example.empsched.shared.configuration.BaseMapperConfig;
import com.example.empsched.shared.dto.employee.CreateEmployeeRequest;
import com.example.empsched.shared.dto.employee.EmployeeResponse;
import com.example.empsched.shared.dto.organisation.CreateOrganisationRequest;
import com.example.empsched.shared.dto.organisation.CreateOrganisationWithOwnerRequest;
import com.example.empsched.shared.dto.organisation.OrganisationResponse;
import com.example.empsched.shared.dto.position.CreatePositionRequest;
import com.example.empsched.shared.dto.position.PositionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Mapper(config = BaseMapperConfig.class)
public interface DtoMapper {
    @Named("mapToEmployeePositionsIds")
    static List<UUID> mapToEmployeePositionsIds(Set<Position> positions) {
        if (positions != null) {
            return positions.stream()
                    .map(Position::getId)
                    .toList();
        }
        return List.of();
    }

    Organisation mapToOrganisation(CreateOrganisationRequest request);

    @Mapping(source = "id", target = "id")
    Organisation mapToOrganisation(UUID id, CreateOrganisationWithOwnerRequest request);

    OrganisationResponse mapToOrganisationResponse(Organisation organisation);

    Employee mapToEmployee(CreateEmployeeRequest request);

    @Mapping(source = "id", target = "id")
    Employee mapToEmployee(UUID id, CreateOrganisationWithOwnerRequest request);

    @Mapping(source = "organisation.id", target = "organisationId")
    EmployeeResponse mapToEmployeeResponse(Employee employee);

    PositionResponse mapToPositionResponse(Position position);

    Position mapToPosition(CreatePositionRequest request);

    Absence mapToAbsence(CreateAbsenceRequest request);

    AbsenceResponse mapToAbsenceResponse(Absence absence);
}
