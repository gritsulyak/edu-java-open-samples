package com.example.payment.infrastructure.persistence.adapter;

import com.example.payment.domain.model.Payment;
import com.example.payment.domain.port.out.PaymentRepository;
import com.example.payment.infrastructure.persistence.entity.PaymentJpaEntity;
import com.example.payment.infrastructure.persistence.repository.PaymentJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentRepositoryAdapter implements PaymentRepository {

    private final PaymentJpaRepository jpaRepository;

    @Override
    public void save(Payment payment) {
        jpaRepository.save(new PaymentJpaEntity(
                payment.id(), payment.amount(), payment.createdAt()));
    }
}
