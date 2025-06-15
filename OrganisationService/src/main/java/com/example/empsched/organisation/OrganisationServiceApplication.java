package com.example.empsched.organisation;

import com.example.empsched.shared.exception.GenericErrorHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({
        GenericErrorHandler.class
})
public class OrganisationServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrganisationServiceApplication.class, args);
    }
}
