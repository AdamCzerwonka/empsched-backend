package com.example.empsched.auth.service.impl;

import com.example.empsched.auth.dto.PushNotificationPayload;
import com.example.empsched.auth.dto.PushNotificationRequest;
import com.example.empsched.auth.dto.PushSubscriptionRequest;
import com.example.empsched.auth.entity.PushSubscription;
import com.example.empsched.auth.entity.User;
import com.example.empsched.auth.repository.PushSubscriptionRepository;
import com.example.empsched.auth.repository.UserRepository;
import com.example.empsched.auth.service.PushSubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;
import java.security.GeneralSecurityException;
import java.security.Security;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class PushSubscriptionServiceImpl implements PushSubscriptionService {

    private final PushSubscriptionRepository pushSubscriptionRepository;
    private final UserRepository userRepository;

    @Value("${push-notifications.vapid.public-key:}")
    private String vapidPublicKey;

    @Value("${push-notifications.vapid.private-key:}")
    private String vapidPrivateKey;

    @Value("${push-notifications.vapid.subject:mailto:admin@empsched.com}")
    private String vapidSubject;

    private PushService pushService;

    private static final Map<String, Map<String, String>> TRANSLATIONS = Map.of(
            "en", Map.of(
                    "notifications.position_assigned.title", "New Position",
                    "notifications.position_assigned.body", "You have been assigned to a new position",
                    "notifications.position_removed.title", "Position Removed",
                    "notifications.position_removed.body", "You have been removed from a position"
            ),
            "pl", Map.of(
                    "notifications.position_assigned.title", "Nowa pozycja",
                    "notifications.position_assigned.body", "Przypisano Ci nową pozycję",
                    "notifications.position_removed.title", "Usunięto pozycję",
                    "notifications.position_removed.body", "Usunięto Ci pozycję"
            )
    );

    @PostConstruct
    public void init() {
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }

        if (vapidPublicKey != null && !vapidPublicKey.isBlank() &&
            vapidPrivateKey != null && !vapidPrivateKey.isBlank()) {
            try {
                pushService = new PushService(vapidPublicKey, vapidPrivateKey, vapidSubject);
                log.info("Web Push service initialized successfully");
            } catch (GeneralSecurityException e) {
                log.error("Failed to initialize Web Push service", e);
            }
        } else {
            log.warn("VAPID keys not configured, push notifications will not work");
        }
    }

    @Override
    public void registerSubscription(UUID userId, PushSubscriptionRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        // Check if subscription already exists
        Optional<PushSubscription> existing = pushSubscriptionRepository.findByEndpoint(request.endpoint());
        if (existing.isPresent()) {
            PushSubscription sub = existing.get();
            sub.setActive(true);
            sub.setLocale(request.locale() != null ? request.locale() : "en");
            pushSubscriptionRepository.save(sub);
            return;
        }

        PushSubscription subscription = new PushSubscription();
        subscription.setId(UUID.randomUUID());
        subscription.setUser(user);
        subscription.setEndpoint(request.endpoint());
        subscription.setP256dh(request.p256dh());
        subscription.setAuth(request.auth());
        subscription.setActive(true);
        subscription.setLocale(request.locale() != null ? request.locale() : "en");

        pushSubscriptionRepository.save(subscription);
        log.info("Push subscription registered for user: {}", userId);
    }

    @Override
    public void unregisterSubscription(String endpoint) {
        pushSubscriptionRepository.deleteByEndpoint(endpoint);
        log.info("Push subscription unregistered: {}", endpoint);
    }

    @Override
    public String getVapidPublicKey() {
        return vapidPublicKey;
    }

    @Override
    public void sendNotification(PushNotificationRequest request) {
        if (pushService == null) {
            log.warn("Push service not initialized, skipping notification");
            return;
        }

        List<PushSubscription> subscriptions = pushSubscriptionRepository.findActiveByUserEmail(request.userEmail());

        for (PushSubscription sub : subscriptions) {
            try {
                String locale = sub.getLocale() != null ? sub.getLocale() : "en";
                Map<String, String> translations = TRANSLATIONS.getOrDefault(locale, TRANSLATIONS.get("en"));

                String title = translations.getOrDefault(request.titleKey(), request.titleKey());
                String body = interpolate(
                        translations.getOrDefault(request.bodyKey(), request.bodyKey()),
                        request.bodyParams()
                );

                PushNotificationPayload payload = new PushNotificationPayload(
                        request.type(),
                        title,
                        body,
                        request.url(),
                        request.data()
                );

                Notification notification = new Notification(
                        sub.getEndpoint(),
                        sub.getP256dh(),
                        sub.getAuth(),
                        payload.toJson().getBytes()
                );

                pushService.send(notification);
                log.debug("Push notification sent to endpoint: {}", sub.getEndpoint());

            } catch (Exception e) {
                log.warn("Push failed for subscription {}: {}", sub.getId(), e.getMessage());
                if (isGoneError(e)) {
                    sub.setActive(false);
                    pushSubscriptionRepository.save(sub);
                    log.info("Deactivated expired subscription: {}", sub.getId());
                }
            }
        }
    }

    private String interpolate(String template, Map<String, String> params) {
        if (template == null || params == null) return template;
        String result = template;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            result = result.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }
        return result;
    }

    private boolean isGoneError(Exception e) {
        String message = e.getMessage();
        return message != null && (message.contains("410") || message.contains("Gone"));
    }
}
