package com.example.billing.domain.model;

/**
 * Parsed domain event arriving from Kafka.
 */
public record PaymentEvent(String id, long amount, String ts) {}
