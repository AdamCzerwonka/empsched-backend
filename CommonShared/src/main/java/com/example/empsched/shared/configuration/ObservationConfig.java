package com.example.empsched.shared.configuration;

import io.micrometer.observation.ObservationPredicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.observation.ServerRequestObservationContext;

@Configuration
public class ObservationConfig {
    @Bean
    ObservationPredicate actuatorServerContextPredicate() {
        return (name, context) -> {
            if (name.equals(
                    "http.server.requests") && context instanceof ServerRequestObservationContext serverContext) {
                String path = serverContext.getCarrier().getRequestURI();
                return !path.startsWith("/actuator");
            }
            return true;
        };
    }
}
