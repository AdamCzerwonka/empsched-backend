package com.example.empsched.workflow;

import com.example.empsched.shared.configuration.BaseSecurityConfig;
import com.example.empsched.shared.configuration.ObservationConfig;
import com.example.empsched.shared.exception.GenericErrorHandler;
import io.temporal.client.WorkflowClient;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Import;

@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class
})
@Import({
        GenericErrorHandler.class,
        ObservationConfig.class,
        BaseSecurityConfig.class
})
@RequiredArgsConstructor
public class WorkflowServiceApplication {
    private final WorkflowClient workflowClient;

    public static void main(String[] args) {
        SpringApplication.run(WorkflowServiceApplication.class, args);
    }
}
