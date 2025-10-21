package com.example.empsched.auth.controller;

import com.example.empsched.auth.dto.LoginRequest;
import com.example.empsched.auth.dto.LoginResponse;
import com.example.empsched.auth.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Transactional(propagation = Propagation.NEVER)
public class LoginController {
    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        String token = loginService.login(request.email(), request.password());
        return ResponseEntity.ok(new LoginResponse(token));
    }
}
