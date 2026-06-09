package com.example.payment.infrastructure.kafka;

import com.example.payment.domain.model.OutboxEvent;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.TopicPartition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("KafkaEventPublisher — unit")
class KafkaEventPublisherTest {

    @Mock
    KafkaTemplate<String, String> kafkaTemplate;
    @InjectMocks
    KafkaEventPublisher publisher;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(publisher, "topic", "payments.created");
    }

    @Test
    @DisplayName("publish() sends to correct topic with aggregateId as key")
    void publish_sendsToTopic() {
        var future = CompletableFuture.<SendResult<String, String>>completedFuture(
                new SendResult<>(
                        new ProducerRecord<>("payments.created", "agg-1", "{}"),
                        new RecordMetadata(new TopicPartition("payments.created", 0),
                                0, 0, 0, 0, 0)));
        when(kafkaTemplate.send(anyString(), anyString(), anyString())).thenReturn(future);

        OutboxEvent event = new OutboxEvent(1L, "agg-1", "PaymentCreated",
                "{}", Instant.now(), null);
        publisher.publish(event);

        verify(kafkaTemplate).send(eq("payments.created"), eq("agg-1"), eq("{}"));
    }
}
