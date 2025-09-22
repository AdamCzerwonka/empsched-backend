package com.example.empsched.auth.service;

import com.example.empsched.auth.entity.User;
import com.example.empsched.shared.dto.user.CreateUserRequest;

import java.util.UUID;

public interface UserService {
    User createUser(final User user);

    void deleteUser(final UUID id);
}
