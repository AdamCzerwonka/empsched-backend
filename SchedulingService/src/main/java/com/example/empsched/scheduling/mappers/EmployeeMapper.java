package com.example.empsched.scheduling.mappers;

import com.example.empsched.scheduling.dto.EmployeeDTO;
import com.example.empsched.scheduling.entity.Employee;
import com.example.empsched.scheduling.entity.Position;
import com.example.empsched.shared.configuration.BaseMapperConfig;
import com.example.empsched.shared.entity.AbstractEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.Set;
import java.util.UUID;

@Mapper(config = BaseMapperConfig.class)
public interface EmployeeMapper {

    @Mappings({
            @Mapping(target = "positionIds", source = "positions"),
            @Mapping(target = "organisationId", source = "organisation")

    })
    EmployeeDTO toDto(Employee schedulingEmployee);

    default UUID entityToUuid(AbstractEntity entity) {
        return entity != null ? entity.getId() : null;
    }

    Set<UUID> mapPositionsSetToPositionIdsSet(Set<Position> positions);


}
