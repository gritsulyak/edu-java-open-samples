package com.example.billing.infrastructure.persistence.adapter;

import com.example.billing.domain.port.out.InboxRepository;
import com.example.billing.infrastructure.persistence.entity.InboxJpaEntity;
import com.example.billing.infrastructure.persistence.repository.InboxJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class InboxRepositoryAdapter implements InboxRepository {

    private final InboxJpaRepository inboxJpaRepository;

    @Override
    public boolean tryInsert(String messageId) {
        try {
            inboxJpaRepository.saveAndFlush(new InboxJpaEntity(messageId, Instant.now()));
            return true;
        } catch (DataIntegrityViolationException ex) {
            // PK violation == duplicate message
            return false;
        }
    }
}
