package com.example.billing.infrastructure.persistence.repository;

import com.example.billing.infrastructure.persistence.entity.InboxJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InboxJpaRepository extends JpaRepository<InboxJpaEntity, String> {
}
