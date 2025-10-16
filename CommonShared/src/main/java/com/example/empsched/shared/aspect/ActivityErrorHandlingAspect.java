package com.example.empsched.shared.aspect;

import com.example.empsched.shared.dto.Problem;
import com.example.empsched.shared.exception.ApplicationException;
import com.example.empsched.shared.exception.GenericErrorHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.temporal.failure.ApplicationFailure;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class ActivityErrorHandlingAspect {
    private final GenericErrorHandler genericErrorHandler;
    private final ObjectMapper objectMapper;

    @Around("@within(io.temporal.spring.boot.ActivityImpl)")
    public Object handleActivityExceptions(ProceedingJoinPoint joinPoint) {
        try {
            return joinPoint.proceed();
        } catch (ApplicationException ex) {
            final ResponseEntity<Problem> response = genericErrorHandler.handleApplicationException(ex);
            String responseBody;
            try {
                responseBody = objectMapper.writeValueAsString(response.getBody());
            } catch (JsonProcessingException e) {
                log.error("Failed to serialize error response: {}", e.getMessage(), e);
                throw createGeneralError();
            }
            throw ApplicationFailure.newNonRetryableFailure(
                    ex.getMessage(),
                    ex.getMessageKey(),
                    response.getStatusCode().value(),
                    responseBody
            );
        } catch (HttpStatusCodeException ex) {
            throw ApplicationFailure.newNonRetryableFailure(
                    ex.getMessage(),
                    "service.client.error",
                    ex.getStatusCode().value(),
                    ex.getResponseBodyAsString()
            );
        } catch (Throwable ex) {
            log.error("Unexpected error occurred: {}", ex.getMessage(), ex);
            throw createGeneralError();
        }
    }

    private ApplicationFailure createGeneralError() {
        return ApplicationFailure.newNonRetryableFailure(
                "An unexpected error occurred.",
                "internal.error",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected error occurred."
        );
    }
}
