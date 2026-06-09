package com.example.payment.application.service;

import com.example.payment.domain.model.OutboxEvent;
import com.example.payment.domain.port.out.EventPublisher;
import com.example.payment.domain.port.out.OutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Outbox worker — polls unprocessed events and forwards them to Kafka.
 * Runs on a fixed-delay schedule (configurable via outbox.worker.interval-ms).
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxDispatchService {

    private final OutboxRepository outboxRepository;
    private final EventPublisher eventPublisher;

    @Value("${outbox.worker.batch-size:100}")
    private int batchSize;

    @Scheduled(fixedDelayString = "${outbox.worker.interval-ms:500}")
    public void dispatch() {
        List<OutboxEvent> events = outboxRepository.findUnprocessedForUpdate(batchSize);
        if (events.isEmpty()) return;

        log.debug("Dispatching {} outbox events", events.size());
        for (OutboxEvent event : events) {
            try {
                eventPublisher.publish(event);
                outboxRepository.markProcessed(event.id());
            } catch (Exception ex) {
                log.error("Failed to publish outbox event id={}: {}", event.id(), ex.getMessage());
                // Next iteration will retry
            }
        }
    }
}
