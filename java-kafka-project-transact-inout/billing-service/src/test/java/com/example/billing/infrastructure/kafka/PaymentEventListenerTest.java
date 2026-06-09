package com.example.billing.infrastructure.kafka;

import com.example.billing.domain.model.PaymentEvent;
import com.example.billing.domain.port.in.ProcessPaymentEventUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.support.Acknowledgment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PaymentEventListener — unit")
class PaymentEventListenerTest {

    @Mock
    ProcessPaymentEventUseCase useCase;
    @Mock
    Acknowledgment ack;
    @Spy
    ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    PaymentEventListener listener;

    private ConsumerRecord<String, String> record(String key, String value) {
        return new ConsumerRecord<>("payments.created", 0, 0L, key, value);
    }

    @Test
    @DisplayName("onMessage() parses payload and delegates to use case")
    void onMessage_validJson_delegatesToUseCase() throws Exception {
        String json = """
                {"id":"pay-1","amount":500,"ts":"2024-01-01T00:00:00Z"}
                """;
        listener.onMessage(record("pay-1", json), ack);

        var captor = ArgumentCaptor.forClass(PaymentEvent.class);
        verify(useCase).execute(eq("pay-1"), captor.capture());
        assertThat(captor.getValue().id()).isEqualTo("pay-1");
        assertThat(captor.getValue().amount()).isEqualTo(500L);
    }

    @Test
    @DisplayName("onMessage() acknowledges after successful processing")
    void onMessage_success_acksOffset() {
        String json = """
                {"id":"pay-2","amount":200,"ts":"2024-01-01T00:00:00Z"}
                """;
        listener.onMessage(record("pay-2", json), ack);

        verify(ack).acknowledge();
    }

    @Test
    @DisplayName("onMessage() does NOT ack on malformed JSON")
    void onMessage_malformedJson_doesNotAck() {
        listener.onMessage(record("pay-3", "not-json"), ack);

        verify(ack, never()).acknowledge();
        verifyNoInteractions(useCase);
    }

    @Test
    @DisplayName("onMessage() does NOT ack when use case throws")
    void onMessage_useCaseThrows_doesNotAck() {
        String json = """
                {"id":"pay-4","amount":100,"ts":"2024-01-01T00:00:00Z"}
                """;
        doThrow(new RuntimeException("db error")).when(useCase).execute(any(), any());

        listener.onMessage(record("pay-4", json), ack);

        verify(ack, never()).acknowledge();
    }
}
