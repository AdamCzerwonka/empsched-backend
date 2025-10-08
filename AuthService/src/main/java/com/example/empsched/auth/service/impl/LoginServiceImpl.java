package com.example.empsched.auth.service.impl;

import com.example.empsched.auth.entity.User;
import com.example.empsched.auth.exception.LoginFailedException;
import com.example.empsched.auth.repository.UserRepository;
import com.example.empsched.auth.service.JwtService;
import com.example.empsched.auth.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public String login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(LoginFailedException::new);

        if (passwordEncoder.matches(password, user.getPassword())) {
            return jwtService.generateToken(email, user.getOrganisationId(), user.getRoles());
        }

        throw new LoginFailedException();
    }
}
