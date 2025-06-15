package com.example.empsched.organisation.configuration;

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
