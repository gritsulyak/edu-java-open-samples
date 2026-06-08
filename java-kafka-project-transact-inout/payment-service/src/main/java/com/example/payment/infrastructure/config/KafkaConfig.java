package com.example.payment.infrastructure.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.Map;

@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        return new DefaultKafkaProducerFactory<>(Map.of(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,        bootstrapServers,
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,     StringSerializer.class,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,   StringSerializer.class,
                ProducerConfig.ACKS_CONFIG,                     "all",
                ProducerConfig.RETRIES_CONFIG,                  10,
                ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG,       true,
                ProducerConfig.COMPRESSION_TYPE_CONFIG,         "lz4",
                ProducerConfig.LINGER_MS_CONFIG,                5,
                ProducerConfig.BATCH_SIZE_CONFIG,               65536,
                ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 5
        ));
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate(ProducerFactory<String, String> pf) {
        return new KafkaTemplate<>(pf);
    }
}
