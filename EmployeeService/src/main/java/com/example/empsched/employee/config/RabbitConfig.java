package com.example.empsched.employee.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange("user-exchange");
    }

    @Bean
    public Queue organisationCreateQueue() {
        return new Queue("organisation.create.queue", true);
    }

    @Bean
    public Binding organisationCreateBinding(TopicExchange topicExchange, Queue organisationCreateQueue) {
        return BindingBuilder
                .bind(organisationCreateQueue)
                .to(topicExchange)
                .with("organisation.create");
    }
}
