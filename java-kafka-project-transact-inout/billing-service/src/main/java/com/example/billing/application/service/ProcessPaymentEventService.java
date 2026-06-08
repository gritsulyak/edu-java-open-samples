package com.example.billing.application.service;

import com.example.billing.domain.model.BillingRecord;
import com.example.billing.domain.model.PaymentEvent;
import com.example.billing.domain.port.in.ProcessPaymentEventUseCase;
import com.example.billing.domain.port.out.BillingRepository;
import com.example.billing.domain.port.out.InboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessPaymentEventService implements ProcessPaymentEventUseCase {

    private final InboxRepository   inboxRepository;
    private final BillingRepository billingRepository;

    @Override
    @Transactional
    public void execute(String messageId, PaymentEvent event) {
        boolean inserted = inboxRepository.tryInsert(messageId);
        if (!inserted) {
            log.info("Duplicate message ignored: {}", messageId);
            return;
        }

        BillingRecord record = BillingRecord.create(event.id(), event.amount());
        billingRepository.save(record);
        log.debug("Billing record saved: id={}, amount={}", record.id(), record.amount());
    }
}
