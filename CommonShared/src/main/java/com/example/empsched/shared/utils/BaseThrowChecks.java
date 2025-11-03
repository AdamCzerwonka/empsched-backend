package com.example.empsched.shared.utils;

import com.example.empsched.shared.exception.ForbiddenOperation;
import com.example.empsched.shared.exception.StartDateAfterEndDateException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BaseThrowChecks {
    public static void throwIfNotRelated(final UUID callerId, final UUID targetId) {
        if (!callerId.equals(targetId)) {
            throw new ForbiddenOperation();
        }
    }

    public static void checkDateValidity(final LocalDate startDate, final LocalDate endDate) {
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new StartDateAfterEndDateException();
        }
    }
}
