package com.example.billing.infrastructure.persistence.repository;

import com.example.billing.infrastructure.persistence.entity.BillingJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillingJpaRepository extends JpaRepository<BillingJpaEntity, String> {
}
