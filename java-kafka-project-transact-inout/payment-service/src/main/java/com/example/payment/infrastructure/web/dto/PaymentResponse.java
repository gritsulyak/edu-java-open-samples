package com.example.payment.infrastructure.web.dto;

import com.example.payment.domain.model.Payment;
import java.time.Instant;

public record PaymentResponse(String id, long amount, Instant createdAt) {
    public static PaymentResponse from(Payment p) {
        return new PaymentResponse(p.id(), p.amount(), p.createdAt());
    }
}
