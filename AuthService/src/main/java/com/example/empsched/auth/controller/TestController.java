package com.example.empsched.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {
    private final RabbitTemplate rabbitTemplate;
    private final Queue queue;

    @GetMapping("/test")
    public String test() {
        String message = "Hello from AuthService!";
        rabbitTemplate.convertAndSend(queue.getName(), message);
        return "AuthService is running!";
    }
}
