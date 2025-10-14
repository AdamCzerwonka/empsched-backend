package com.example.empsched.shared.client;

import com.example.empsched.shared.utils.RequestContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.temporal.failure.ApplicationFailure;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class ServiceClient {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${services.auth.url}")
    private String authServiceUrl;

    @Value("${services.organisation.url}")
    private String organisationServiceUrl;

    @Value("${services.employee.url}")
    private String employeeServiceUrl;


    public <T, R> ResponseEntity<R> sendRequest(final ServiceType service, final String path, final HttpMethod method, final T payload, final Class<R> responseType) {
        return sendRequest(service, path, method, payload, responseType, null);
    }

    public <T, R> ResponseEntity<R> sendRequest(final ServiceType service, final String path, final HttpMethod method, final T payload, final Class<R> responseType, final RequestContext requestContext) {
        final String serviceUrl = getServiceUrl(service);
        final String url = serviceUrl + path;

        final HttpHeaders headers = prepareHeaders(requestContext);
        final HttpEntity<T> requestEntity = new HttpEntity<>(payload, headers);

        try {
            return restTemplate.exchange(url, method, requestEntity, responseType);
        } catch (HttpStatusCodeException e) {
            final int responseStatus = e.getStatusCode().value();
            final String responseBody = e.getResponseBodyAsString();
            throw ApplicationFailure.newNonRetryableFailure(
                    String.format("Service call to %s failed. Status: %s Message: %s", service, responseStatus, responseBody),
                    "",
                    responseStatus,
                    responseBody
            );
        }
    }

    private HttpHeaders prepareHeaders(final RequestContext requestContext) {
        final HttpHeaders headers = new HttpHeaders();
        if (requestContext != null) {
            if (requestContext.getAuthorizationHeader() != null) {
                headers.set(HttpHeaders.AUTHORIZATION, requestContext.getAuthorizationHeader());
            }
        }
        return headers;
    }

    private String getServiceUrl(final ServiceType service) {
        return switch (service) {
            case AUTH -> authServiceUrl;
            case ORGANISATION -> organisationServiceUrl;
            case EMPLOYEE -> employeeServiceUrl;
        };
    }
}
