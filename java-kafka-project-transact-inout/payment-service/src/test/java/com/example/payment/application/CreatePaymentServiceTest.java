package com.example.payment.application;

import com.example.payment.application.service.CreatePaymentService;
import com.example.payment.domain.model.Payment;
import com.example.payment.domain.port.out.OutboxRepository;
import com.example.payment.domain.port.out.PaymentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreatePaymentService — unit")
class CreatePaymentServiceTest {

    @Mock  PaymentRepository paymentRepository;
    @Mock  OutboxRepository  outboxRepository;
    @Spy   ObjectMapper      objectMapper = new ObjectMapper();

    @InjectMocks
    CreatePaymentService service;

    @Test
    @DisplayName("execute() saves payment and outbox event in same call")
    void execute_savesBoth() {
        Payment result = service.execute("pay-42", 300L);

        assertThat(result.id()).isEqualTo("pay-42");
        assertThat(result.amount()).isEqualTo(300L);

        verify(paymentRepository, times(1)).save(argThat(p -> p.id().equals("pay-42")));
        verify(outboxRepository,  times(1)).save(argThat(e ->
                e.aggregateId().equals("pay-42") && e.eventType().equals("PaymentCreated")));
    }

    @Test
    @DisplayName("execute() outbox payload contains id and amount")
    void execute_outboxPayloadContainsFields() throws Exception {
        var captor = ArgumentCaptor.forClass(com.example.payment.domain.model.OutboxEvent.class);

        service.execute("pay-99", 777L);
        verify(outboxRepository).save(captor.capture());

        String payload = captor.getValue().payload();
        assertThat(payload).contains("pay-99").contains("777");
    }

    @Test
    @DisplayName("execute() propagates domain validation error")
    void execute_invalidAmount_throws() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> service.execute("pay-1", -5L));

        verifyNoInteractions(paymentRepository, outboxRepository);
    }
}
