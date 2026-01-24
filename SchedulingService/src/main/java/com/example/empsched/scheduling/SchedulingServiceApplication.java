package com.example.empsched.scheduling;

import com.example.empsched.shared.client.AuthServiceClient;
import com.example.empsched.shared.client.ServiceClient;
import com.example.empsched.shared.configuration.BaseSecurityConfig;
import com.example.empsched.shared.configuration.MapperConfiguration;
import com.example.empsched.shared.configuration.ObservationConfig;
import com.example.empsched.shared.configuration.RestConfig;
import com.example.empsched.shared.exception.GenericErrorHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({
		GenericErrorHandler.class,
		ObservationConfig.class,
		BaseSecurityConfig.class,
		RestConfig.class,
		ServiceClient.class,
		AuthServiceClient.class,
		MapperConfiguration.class
})
public class SchedulingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SchedulingServiceApplication.class, args);
	}

}
