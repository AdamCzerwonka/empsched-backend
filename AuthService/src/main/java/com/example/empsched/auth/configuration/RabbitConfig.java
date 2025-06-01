package com.example.empsched.auth.configuration;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange("user-exchange");
    }

    @Bean
    public Queue userCreationQueue() {
        return new Queue("user.creation.queue", true);
    }

    @Bean
    public Binding userCreationBinding(TopicExchange topicExchange, Queue userCreationQueue) {
        return BindingBuilder
                .bind(userCreationQueue)
                .to(topicExchange)
                .with("user.create");
    }
}
