package com.example.empsched.employee.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange("user-exchange");
    }
}
