package com.example.payment.domain.model;

import java.time.Instant;

/**
 * Transactional outbox event — domain concept, not a persistence entity.
 */
public record OutboxEvent(
        Long    id,
        String  aggregateId,
        String  eventType,
        String  payload,
        Instant createdAt,
        Instant processedAt
) {}
