package com.example.empsched.shared.client;

import com.example.empsched.shared.dto.user.CreateUserRequest;
import com.example.empsched.shared.dto.user.UserResponse;
import com.example.empsched.shared.util.RequestContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AuthServiceClient {
    private static final ServiceType SERVICE_TYPE = ServiceType.AUTH;

    private final ServiceClient serviceClient;

    public ResponseEntity<UserResponse> createUser(final CreateUserRequest request) {
        return serviceClient.sendRequest(SERVICE_TYPE, "/users", HttpMethod.POST, request, UserResponse.class);
    }

    public ResponseEntity<UserResponse> createUser(final CreateUserRequest request, final RequestContext context) {
        return serviceClient.sendRequest(SERVICE_TYPE, "/users", HttpMethod.POST, request, UserResponse.class, context);
    }

    public ResponseEntity<Void> deleteUser(final UUID id) {
        return serviceClient.sendRequest(SERVICE_TYPE, "/users/" + id, HttpMethod.DELETE, null, Void.class);
    }
}
