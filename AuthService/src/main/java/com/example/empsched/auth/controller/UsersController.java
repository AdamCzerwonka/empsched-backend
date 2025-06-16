package com.example.empsched.auth.controller;

import com.example.empsched.auth.dto.UserDto;
import com.example.empsched.auth.entity.User;
import com.example.empsched.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UsersController {
    private final UserRepository userRepository;

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDto>> getUsers() {
        return ResponseEntity.ok(userRepository.findAll().stream().map(user -> new UserDto(user.getId(),
                user.getEmail(), user.getRoles())).toList());
    }
}
