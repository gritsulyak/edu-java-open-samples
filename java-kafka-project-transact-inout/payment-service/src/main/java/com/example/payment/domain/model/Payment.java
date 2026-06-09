package com.example.payment.domain.model;

import java.time.Instant;

/**
 * Aggregate root.  Pure domain object — no framework annotations.
 */
public record Payment(
        String id,
        long amount,
        Instant createdAt
) {
    public Payment {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("id must not be blank");
        if (amount <= 0) throw new IllegalArgumentException("amount must be positive");
    }

    public static Payment create(String id, long amount) {
        return new Payment(id, amount, Instant.now());
    }
}
