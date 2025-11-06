package com.example.empsched.shared.util;

import io.temporal.activity.ActivityOptions;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Duration;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomActivityOptions {
    public static final ActivityOptions DEFAULT = ActivityOptions.newBuilder()
            .setStartToCloseTimeout(Duration.ofSeconds(10))
            .setRetryOptions(io.temporal.common.RetryOptions.newBuilder()
                    .setInitialInterval(Duration.ofSeconds(2))
                    .setMaximumInterval(Duration.ofSeconds(10))
                    .setMaximumAttempts(3)
                    .build())
            .build();
}
