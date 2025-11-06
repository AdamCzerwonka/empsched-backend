package com.example.empsched.auth.service.impl;

import com.example.empsched.auth.entity.User;
import com.example.empsched.auth.exception.InvalidCredentialsException;
import com.example.empsched.auth.repository.UserRepository;
import com.example.empsched.auth.service.JwtService;
import com.example.empsched.auth.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class LoginServiceImpl implements LoginService {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public String login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(InvalidCredentialsException::new);

        if (passwordEncoder.matches(password, user.getPassword())) {
            return jwtService.generateToken(user.getId(), email, user.getOrganisationId(), user.getRoles());
        }

        throw new InvalidCredentialsException();
    }
}
