package com.example.empsched.shared.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrganisationServiceClient {
    private static final ServiceType SERVICE_TYPE = ServiceType.ORGANISATION;

    private final ServiceClient serviceClient;
}
