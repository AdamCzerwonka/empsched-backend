package com.example.empsched.auth.service.impl;

import com.example.empsched.auth.entity.User;
import com.example.empsched.auth.repository.UserRepository;
import com.example.empsched.auth.service.UserService;
import com.example.empsched.shared.dto.UserCreateEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void createUser(UserCreateEvent userDto) {
        String hashedPassword = passwordEncoder.encode(userDto.password());

        User user = new User(userDto.id(), userDto.email(), hashedPassword);
        userRepository.save(user);
    }
}
