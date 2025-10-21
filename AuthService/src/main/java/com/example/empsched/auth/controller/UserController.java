package com.example.empsched.auth.controller;

import com.example.empsched.auth.dto.UserDto;
import com.example.empsched.auth.entity.User;
import com.example.empsched.auth.mapper.DtoMapper;
import com.example.empsched.auth.repository.UserRepository;
import com.example.empsched.auth.service.UserService;
import com.example.empsched.shared.dto.user.CreateUserRequest;
import com.example.empsched.shared.dto.user.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Transactional(propagation = Propagation.NEVER)
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final DtoMapper mapper;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody @Valid final CreateUserRequest request) {
        final User user = userService.createUser(mapper.mapToUser(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.mapToUserResponse(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable final UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDto>> getUsers() {
        return ResponseEntity.ok(userRepository.findAll().stream().map(user -> new UserDto(user.getId(),
                user.getEmail(), user.getRoles())).toList());
    }
}
