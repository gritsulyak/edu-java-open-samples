package com.example.payment.infrastructure.persistence.repository;

import com.example.payment.infrastructure.persistence.entity.OutboxJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OutboxJpaRepository extends JpaRepository<OutboxJpaEntity, Long> {

    @Query("SELECT o FROM OutboxJpaEntity o WHERE o.processedAt IS NULL ORDER BY o.id LIMIT :limit")
    List<OutboxJpaEntity> findUnprocessed(@Param("limit") int limit);
}
