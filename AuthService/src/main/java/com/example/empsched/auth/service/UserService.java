package com.example.empsched.auth.service;

import com.example.empsched.shared.dto.UserCreateEventDto;

public interface UserService {
    void createUser(UserCreateEventDto user);
}
