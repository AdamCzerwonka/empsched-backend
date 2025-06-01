package com.example.empsched.auth.listener;

import com.example.empsched.auth.service.UserService;
import com.example.empsched.shared.dto.UserCreateEventDto;
import com.example.empsched.shared.utils.SerializerUtils;
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
    public void handleUserCreation(String message) {
        UserCreateEventDto user = SerializerUtils.deserialize(message, UserCreateEventDto.class);

        userService.createUser(user);
        log.info("User created: {}", user);
    }
}
