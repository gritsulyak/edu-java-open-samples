package com.example.billing.domain.port.in;

import com.example.billing.domain.model.PaymentEvent;

public interface ProcessPaymentEventUseCase {
    /**
     * @param messageId Kafka message key — used for inbox idempotency check
     * @param event     Parsed payment domain event
     */
    void execute(String messageId, PaymentEvent event);
}
