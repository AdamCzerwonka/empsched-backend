package com.example.empsched.employee.service.impl;

import com.example.empsched.employee.service.EmployeeService;
import com.example.empsched.shared.dto.UserCreateEvent;
import com.example.empsched.shared.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final RabbitTemplate rabbitTemplate;
    private final TopicExchange topicExchange;

    @Override
    public void createEmployee(String email, String password) {
        UUID uuid = UUID.randomUUID();
        UserCreateEvent userCreateEvent = UserCreateEvent.builder()
                .id(uuid)
                .email(email)
                .password(password)
                .role(Role.ORGANISATION_EMPLOYEE)
                .build();

        rabbitTemplate.convertAndSend(topicExchange.getName(), "user.create", userCreateEvent);
    }
}
