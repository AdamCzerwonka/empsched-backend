package com.example.empsched.shared.rabbit;

public class RoutingKeys {
    private RoutingKeys() {
        throw new IllegalStateException("Utility class");
    }

    public static final String ORGANISATION = "organisation";
    public static final String ORGANISATION_CREATE = ORGANISATION + ".create";

    public static final String USER = "user";
    public static final String USER_CREATE = USER + ".create";
}
