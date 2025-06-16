package com.example.empsched.auth.service.impl;

import com.example.empsched.auth.entity.User;
import com.example.empsched.auth.exception.UserAlreadyExists;
import com.example.empsched.auth.repository.UserRepository;
import com.example.empsched.auth.service.UserService;
import com.example.empsched.shared.dto.UserCreateEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void createUser(UserCreateEvent userDto) {
        String hashedPassword = passwordEncoder.encode(userDto.password());

        userRepository.findByEmail(userDto.email())
                .ifPresent(existingUser -> {
                    throw new UserAlreadyExists(existingUser.getEmail());
                });

        User user = new User(userDto.id(), userDto.email(), hashedPassword);
        user.getRoles().add(userDto.role());
        userRepository.save(user);
    }
}
