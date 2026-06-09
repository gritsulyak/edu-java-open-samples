package com.example.payment.infrastructure.persistence.adapter;

import com.example.payment.domain.model.OutboxEvent;
import com.example.payment.domain.port.out.OutboxRepository;
import com.example.payment.infrastructure.persistence.entity.OutboxJpaEntity;
import com.example.payment.infrastructure.persistence.repository.OutboxJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OutboxRepositoryAdapter implements OutboxRepository {

    private final OutboxJpaRepository jpaRepository;

    @Override
    public void save(OutboxEvent event) {
        jpaRepository.save(new OutboxJpaEntity(
                null,
                event.aggregateId(),
                event.eventType(),
                event.payload(),
                event.createdAt(),
                null));
    }

    @Override
    public List<OutboxEvent> findUnprocessedForUpdate(int limit) {
        return jpaRepository.findUnprocessed(limit).stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markProcessed(long id) {
        jpaRepository.findById(id).ifPresent(entity -> {
            entity.setProcessedAt(Instant.now());
            jpaRepository.save(entity);
        });
    }

    private OutboxEvent toDomain(OutboxJpaEntity e) {
        return new OutboxEvent(e.getId(), e.getAggregateId(), e.getEventType(),
                e.getPayload(), e.getCreatedAt(), e.getProcessedAt());
    }
}
