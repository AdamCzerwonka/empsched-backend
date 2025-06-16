package com.example.empsched.auth.listener;

import com.example.empsched.auth.service.UserService;
import com.example.empsched.shared.dto.UserCreateEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


@Component
@Slf4j
@RequiredArgsConstructor
public class UserCreateListener {
    private final UserService userService;

    @RabbitListener(queues = "user.creation.queue")
    public void handleUserCreation(UserCreateEvent user) {
        userService.createUser(user);
    }
}
