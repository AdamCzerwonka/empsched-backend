package com.example.empsched.auth.service;

import com.example.empsched.shared.dto.UserCreateEvent;

public interface UserService {
    void createUser(UserCreateEvent user);
}
