package com.example.empsched.auth;

import com.example.empsched.shared.exception.GenericErrorHandler;
import com.example.empsched.shared.rabbit.RabbitBaseConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({
        GenericErrorHandler.class,
        RabbitBaseConfiguration.class,
})
public class AuthServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }
}
