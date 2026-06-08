package com.example.billing.domain.model;

import java.time.Instant;

public record BillingRecord(
        String  id,
        long    amount,
        Instant createdAt
) {
    public BillingRecord {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("id must not be blank");
        if (amount <= 0)                throw new IllegalArgumentException("amount must be positive");
    }

    public static BillingRecord create(String id, long amount) {
        return new BillingRecord(id, amount, Instant.now());
    }
}
