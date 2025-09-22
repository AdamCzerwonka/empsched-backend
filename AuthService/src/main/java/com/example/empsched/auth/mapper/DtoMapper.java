package com.example.empsched.auth.mapper;

import com.example.empsched.auth.entity.User;
import com.example.empsched.shared.configuration.BaseMapperConfig;
import com.example.empsched.shared.dto.user.CreateUserRequest;
import com.example.empsched.shared.dto.user.UserResponse;
import org.mapstruct.Mapper;

@Mapper(config = BaseMapperConfig.class)
public interface DtoMapper {
    User mapToUser(CreateUserRequest request);

    UserResponse mapToUserResponse(User user);
}
