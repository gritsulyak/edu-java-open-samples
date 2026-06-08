package com.example.billing.application;

import com.example.billing.application.service.ProcessPaymentEventService;
import com.example.billing.domain.model.PaymentEvent;
import com.example.billing.domain.port.out.BillingRepository;
import com.example.billing.domain.port.out.InboxRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProcessPaymentEventService — unit")
class ProcessPaymentEventServiceTest {

    @Mock InboxRepository   inboxRepository;
    @Mock BillingRepository billingRepository;

    @InjectMocks ProcessPaymentEventService service;

    private final PaymentEvent event = new PaymentEvent("pay-1", 100L, "2024-01-01T00:00:00Z");

    @Test
    @DisplayName("execute() saves billing record for new message")
    void execute_newMessage_savesBillingRecord() {
        when(inboxRepository.tryInsert("msg-1")).thenReturn(true);

        service.execute("msg-1", event);

        verify(billingRepository).save(argThat(r ->
                r.id().equals("pay-1") && r.amount() == 100L));
    }

    @Test
    @DisplayName("execute() skips billing for duplicate message")
    void execute_duplicateMessage_skips() {
        when(inboxRepository.tryInsert("msg-1")).thenReturn(false);

        service.execute("msg-1", event);

        verifyNoInteractions(billingRepository);
    }

    @Test
    @DisplayName("execute() calls tryInsert before saving")
    void execute_inboxCheckedFirst() {
        var order = inOrder(inboxRepository, billingRepository);
        when(inboxRepository.tryInsert(any())).thenReturn(true);

        service.execute("msg-2", event);

        order.verify(inboxRepository).tryInsert("msg-2");
        order.verify(billingRepository).save(any());
    }
}
