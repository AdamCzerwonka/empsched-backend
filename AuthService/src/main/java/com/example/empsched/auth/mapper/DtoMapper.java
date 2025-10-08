package com.example.empsched.auth.mapper;

import com.example.empsched.auth.entity.User;
import com.example.empsched.shared.configuration.BaseMapperConfig;
import com.example.empsched.shared.dto.user.CreateUserRequest;
import com.example.empsched.shared.dto.user.UserResponse;
import com.example.empsched.shared.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(config = BaseMapperConfig.class)
public interface DtoMapper {
    @Named("mapRole")
    static List<Role> mapRole(Role role) {
        if (role != null) {
            return List.of(role);
        }
        return List.of();
    }

    @Mapping(source = "role", target = "roles", qualifiedByName = "mapRole")
    User mapToUser(CreateUserRequest request);

    UserResponse mapToUserResponse(User user);
}
