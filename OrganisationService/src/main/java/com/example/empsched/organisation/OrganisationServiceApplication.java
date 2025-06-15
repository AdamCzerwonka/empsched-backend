package com.example.empsched.organisation;

import com.example.empsched.shared.configuration.ObservationConfig;
import com.example.empsched.shared.exception.GenericErrorHandler;
import com.example.empsched.shared.rabbit.RabbitBaseConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({
        GenericErrorHandler.class,
        RabbitBaseConfiguration.class,
        ObservationConfig.class
})
public class OrganisationServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrganisationServiceApplication.class, args);
    }
}
