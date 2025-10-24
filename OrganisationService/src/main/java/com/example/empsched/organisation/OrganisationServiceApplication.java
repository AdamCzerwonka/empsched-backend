package com.example.empsched.organisation;

import com.example.empsched.shared.aspect.ActivityErrorHandlingAspect;
import com.example.empsched.shared.client.AuthServiceClient;
import com.example.empsched.shared.client.EmployeeServiceClient;
import com.example.empsched.shared.client.ServiceClient;
import com.example.empsched.shared.configuration.BaseSecurityConfig;
import com.example.empsched.shared.configuration.MapperConfiguration;
import com.example.empsched.shared.configuration.ObservationConfig;
import com.example.empsched.shared.configuration.RestConfig;
import com.example.empsched.shared.exception.GenericErrorHandler;
import io.temporal.client.WorkflowClient;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({
        GenericErrorHandler.class,
        ObservationConfig.class,
        BaseSecurityConfig.class,
        RestConfig.class,
        ServiceClient.class,
        EmployeeServiceClient.class,
        AuthServiceClient.class,
        ActivityErrorHandlingAspect.class,
        MapperConfiguration.class
})
@RequiredArgsConstructor
@EnableAspectJAutoProxy
public class OrganisationServiceApplication {
    private final WorkflowClient workflowClient;

    public static void main(String[] args) {
        SpringApplication.run(OrganisationServiceApplication.class, args);
    }
}
