package com.example.empsched.scheduling.mappers;

import com.example.empsched.scheduling.dto.ScheduleResponse;
import com.example.empsched.scheduling.dto.ShiftResponse;
import com.example.empsched.scheduling.entity.Employee;
import com.example.empsched.scheduling.entity.EmployeeAvailability;
import com.example.empsched.scheduling.entity.Organisation;
import com.example.empsched.scheduling.entity.Position;
import com.example.empsched.scheduling.entity.Schedule;
import com.example.empsched.scheduling.entity.Shift;
import com.example.empsched.shared.configuration.BaseMapperConfig;
import com.example.empsched.shared.dto.organisation.OrganisationResponse;
import com.example.empsched.shared.dto.position.PositionResponse;
import com.example.empsched.shared.dto.scheduling.EmployeeAvailabilityResponse;
import com.example.empsched.shared.dto.scheduling.SchedulingEmployeeResponse;
import com.example.empsched.shared.entity.AbstractEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Mapper(config = BaseMapperConfig.class)
public interface DtoMapper {

    @Mapping(target = "positionIds", source = "positions")
    @Mapping(target = "organisationId", source = "organisation")
    SchedulingEmployeeResponse toDto(Employee schedulingEmployee);

    List<SchedulingEmployeeResponse> toDtoList(List<Employee> employees);

    Set<UUID> mapPositionsSetToPositionIdsSet(Set<Position> positions);

    OrganisationResponse toResponse(Organisation organisation);

    PositionResponse toResponse(Position entity);

    List<PositionResponse> toPositionResponseList(List<Position> entities);

    ScheduleResponse toDto(Schedule schedule);

    ShiftResponse toDto(Shift shift);

    List<ShiftResponse> toShiftDtoList(List<Shift> shifts);

    EmployeeAvailabilityResponse toResponse(EmployeeAvailability entity);

    List<EmployeeAvailabilityResponse> toAvailabilityResponseList(List<EmployeeAvailability> entities);

    default UUID entityToUuid(AbstractEntity entity) {
        return entity != null ? entity.getId() : null;
    }
}
