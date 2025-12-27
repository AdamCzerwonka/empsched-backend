package com.example.empsched.gateway;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ApiGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth-service", r -> r.path("/auth/**")
                        .filters(f -> f.rewritePath("/auth/(?<segment>.*)", "/${segment}"))
                        .uri("lb://auth-service"))
                .route("employee-service", r -> r.path("/employees/**")
                        .filters(f -> f.rewritePath("/employees/(?<segment>.*)", "/${segment}"))
                        .uri("lb://employee-service"))
                .route("organisation-service", r -> r.path("/organisations/**")
                        .filters(f -> f.rewritePath("/organisations/(?<segment>.*)", "/${segment}"))
                        .uri("lb://organisation-service"))
                .route("scheduling-service", r -> r.path("/scheduling/**")
                        .filters(f -> f.rewritePath("/scheduling/(?<segment>.*)", "/${segment}"))
                        .uri("lb://scheduling-service"))
                .build();
    }
}
