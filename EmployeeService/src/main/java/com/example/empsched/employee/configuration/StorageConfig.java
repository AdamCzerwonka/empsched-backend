package com.example.empsched.employee.configuration;

import com.google.cloud.NoCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class StorageConfig {
    @Bean
    @Profile("prod")
    public Storage localStorage(
            @Value("${gcs.mock-host}") String host,
            @Value("${gcs.project-id}") String projectId) {
        return StorageOptions.newBuilder()
                .setHost(host)
                .setProjectId(projectId)
                .setCredentials(NoCredentials.getInstance())
                .build()
                .getService();
    }

    // @Bean
    // @Profile("prod")
    // public Storage cloudStorage() {
    // return StorageOptions.getDefaultInstance().getService();
    // }
}
