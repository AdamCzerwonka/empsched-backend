package com.example.empsched.auth.controller;

import com.example.empsched.auth.dto.PushNotificationRequest;
import com.example.empsched.auth.dto.PushSubscriptionRequest;
import com.example.empsched.auth.dto.PushSubscriptionUnregisterRequest;
import com.example.empsched.auth.dto.VapidPublicKeyResponse;
import com.example.empsched.auth.service.PushSubscriptionService;
import com.example.empsched.shared.util.CredentialsExtractor;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/push-subscriptions")
@RequiredArgsConstructor
@Slf4j
public class PushSubscriptionController {

    private final PushSubscriptionService pushSubscriptionService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> registerSubscription(
            @Valid @RequestBody PushSubscriptionRequest request) {
        pushSubscriptionService.registerSubscription(CredentialsExtractor.getUserIdFromContext(), request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> unregisterSubscription(
            @Valid @RequestBody PushSubscriptionUnregisterRequest request) {
        pushSubscriptionService.unregisterSubscription(request.endpoint());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/vapid-public-key")
    public ResponseEntity<VapidPublicKeyResponse> getVapidPublicKey() {
        String publicKey = pushSubscriptionService.getVapidPublicKey();
        return ResponseEntity.ok(new VapidPublicKeyResponse(publicKey));
    }

    @PostMapping("/notify")
    public ResponseEntity<Void> sendNotification(
            @Valid @RequestBody PushNotificationRequest request) {
        log.info("Received push notification request for user: {}", request.userEmail());
        pushSubscriptionService.sendNotification(request);
        return ResponseEntity.ok().build();
    }
}
