package com.example.empsched.shared.client;

import com.example.empsched.shared.util.RequestContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class ServiceClient {
    private final RestTemplate restTemplate;

    @Value("${services.auth.url}")
    private String authServiceUrl;

    @Value("${services.organisation.url}")
    private String organisationServiceUrl;

    @Value("${services.employee.url}")
    private String employeeServiceUrl;

    @Value("${services.scheduling.url}")
    private String schedulingServiceUrl;

    public <T, R> ResponseEntity<R> sendRequest(final ServiceType service, final String path, final HttpMethod method, final T payload, final Class<R> responseType) {
        return sendRequest(service, path, method, payload, responseType, null);
    }

    public <T, R> ResponseEntity<R> sendRequest(final ServiceType service, final String path, final HttpMethod method, final T payload, final Class<R> responseType, final RequestContext requestContext) {
        final String serviceUrl = getServiceUrl(service);
        final String url = serviceUrl + path;

        final HttpHeaders headers = prepareHeaders(requestContext);
        final HttpEntity<T> requestEntity = new HttpEntity<>(payload, headers);

        return restTemplate.exchange(url, method, requestEntity, responseType);
    }

    private HttpHeaders prepareHeaders(final RequestContext requestContext) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
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
            case SCHEDULING -> schedulingServiceUrl;
        };
    }
}
