package com.example.empsched.employee;

import com.example.empsched.shared.configuration.BaseSecurityConfig;
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
        ObservationConfig.class,
        BaseSecurityConfig.class
})
public class EmployeeServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(EmployeeServiceApplication.class, args);
    }
}
