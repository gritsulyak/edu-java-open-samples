package com.example.payment.application;

import com.example.payment.application.service.OutboxDispatchService;
import com.example.payment.domain.model.OutboxEvent;
import com.example.payment.domain.port.out.EventPublisher;
import com.example.payment.domain.port.out.OutboxRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("OutboxDispatchService — unit")
class OutboxDispatchServiceTest {

    @Mock
    OutboxRepository outboxRepository;
    @Mock
    EventPublisher eventPublisher;

    @InjectMocks
    OutboxDispatchService service;

    private OutboxEvent event(long id) {
        return new OutboxEvent(id, "agg-" + id, "PaymentCreated",
                "{}", Instant.now(), null);
    }

    @Test
    @DisplayName("dispatch() publishes each event and marks processed")
    void dispatch_publishesAndMarks() {
        when(outboxRepository.findUnprocessedForUpdate(anyInt()))
                .thenReturn(List.of(event(1L), event(2L)));

        service.dispatch();

        verify(eventPublisher, times(2)).publish(any());
        verify(outboxRepository).markProcessed(1L);
        verify(outboxRepository).markProcessed(2L);
    }

    @Test
    @DisplayName("dispatch() skips mark when publish fails")
    void dispatch_publishFails_doesNotMark() {
        when(outboxRepository.findUnprocessedForUpdate(anyInt()))
                .thenReturn(List.of(event(10L)));
        doThrow(new RuntimeException("kafka down")).when(eventPublisher).publish(any());

        service.dispatch();   // must not throw

        verify(outboxRepository, never()).markProcessed(anyLong());
    }

    @Test
    @DisplayName("dispatch() does nothing when no unprocessed events")
    void dispatch_emptyList_noInteractions() {
        when(outboxRepository.findUnprocessedForUpdate(anyInt())).thenReturn(List.of());

        service.dispatch();

        verifyNoInteractions(eventPublisher);
        verify(outboxRepository, never()).markProcessed(anyLong());
    }
}
