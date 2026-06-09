package com.example.payment.application.service;

import com.example.payment.domain.model.OutboxEvent;
import com.example.payment.domain.model.Payment;
import com.example.payment.domain.port.in.CreatePaymentUseCase;
import com.example.payment.domain.port.out.OutboxRepository;
import com.example.payment.domain.port.out.PaymentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CreatePaymentService implements CreatePaymentUseCase {

    private final PaymentRepository paymentRepository;
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public Payment execute(String id, long amount) {
        Payment payment = Payment.create(id, amount);

        paymentRepository.save(payment);

        outboxRepository.save(buildOutboxEvent(payment));

        return payment;
    }

    @SneakyThrows
    private OutboxEvent buildOutboxEvent(Payment payment) {
        String payload = objectMapper.writeValueAsString(
                Map.of("id", payment.id(),
                        "amount", payment.amount(),
                        "ts", payment.createdAt().toString()));

        return new OutboxEvent(null, payment.id(), "PaymentCreated",
                payload, payment.createdAt(), null);
    }
}
