package com.example.empsched.shared.utils;

import com.example.empsched.shared.exception.ForbiddenOperation;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BaseThrowChecks {
    public static void throwIfNotRelated(final UUID callerId, final UUID targetId) {
        if (!callerId.equals(targetId)) {
            throw new ForbiddenOperation();
        }
    }
}
