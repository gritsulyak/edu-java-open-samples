package com.example.billing.infrastructure.persistence.adapter;

import com.example.billing.domain.model.BillingRecord;
import com.example.billing.domain.port.out.BillingRepository;
import com.example.billing.infrastructure.persistence.entity.BillingJpaEntity;
import com.example.billing.infrastructure.persistence.repository.BillingJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BillingRepositoryAdapter implements BillingRepository {

    private final BillingJpaRepository billingJpaRepository;

    @Override
    public void save(BillingRecord record) {
        billingJpaRepository.save(new BillingJpaEntity(record.id(), record.amount(), record.createdAt()));
    }
}
