package com.example.empsched.auth.service;

import com.example.empsched.auth.dto.PushNotificationRequest;
import com.example.empsched.auth.dto.PushSubscriptionRequest;

import java.util.UUID;

public interface PushSubscriptionService {

    void registerSubscription(UUID userId, PushSubscriptionRequest request);

    void unregisterSubscription(String endpoint);

    String getVapidPublicKey();

    void sendNotification(PushNotificationRequest request);
}
