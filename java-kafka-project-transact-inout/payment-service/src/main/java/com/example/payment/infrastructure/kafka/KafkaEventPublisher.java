package com.example.payment.infrastructure.kafka;

import com.example.payment.domain.model.OutboxEvent;
import com.example.payment.domain.port.out.EventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaEventPublisher implements EventPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${outbox.worker.topic:payments.created}")
    private String topic;

    @Override
    public void publish(OutboxEvent event) {
        kafkaTemplate.send(topic, event.aggregateId(), event.payload())
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Delivery failed for aggregateId={}: {}", event.aggregateId(), ex.getMessage());
                    } else {
                        log.debug("Delivered aggregateId={} to partition {}",
                                event.aggregateId(),
                                result.getRecordMetadata().partition());
                    }
                });
    }
}
