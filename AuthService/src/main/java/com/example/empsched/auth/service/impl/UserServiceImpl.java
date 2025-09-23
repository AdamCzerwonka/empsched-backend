package com.example.empsched.auth.service.impl;

import com.example.empsched.auth.entity.User;
import com.example.empsched.auth.exception.UserAlreadyExists;
import com.example.empsched.auth.repository.UserRepository;
import com.example.empsched.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User createUser(final User user) {
        final String hashedPassword = passwordEncoder.encode(user.getPassword());

        userRepository.findByEmail(user.getEmail())
                .ifPresent(existingUser -> {
                    throw new UserAlreadyExists(existingUser.getEmail());
                });

        user.setPassword(hashedPassword);
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(final UUID id) {
        userRepository.deleteById(id);
    }
}
