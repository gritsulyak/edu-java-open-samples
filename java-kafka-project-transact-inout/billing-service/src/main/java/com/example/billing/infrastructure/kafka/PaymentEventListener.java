package com.example.billing.infrastructure.kafka;

import com.example.billing.domain.model.PaymentEvent;
import com.example.billing.domain.port.in.ProcessPaymentEventUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventListener {

    private final ProcessPaymentEventUseCase processPaymentEventUseCase;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "payments.created", groupId = "${spring.kafka.consumer.group-id}")
    public void onMessage(ConsumerRecord<String, String> record, Acknowledgment ack) {
        String messageId = record.key();
        log.debug("Received message key={} partition={} offset={}",
                messageId, record.partition(), record.offset());

        try {
            PaymentEvent event = objectMapper.readValue(record.value(), PaymentEvent.class);
            processPaymentEventUseCase.execute(messageId, event);
            ack.acknowledge(); // manual commit only after successful processing
        } catch (Exception ex) {
            log.error("Failed to process message key={}: {}", messageId, ex.getMessage(), ex);
            // Do NOT ack — message will be redelivered
        }
    }
}
