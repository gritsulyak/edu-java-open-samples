package com.example.billing.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "inbox")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class InboxJpaEntity {

    @Id
    @Column(name = "message_id", nullable = false, length = 255)
    private String messageId;

    @Column(name = "received_at", nullable = false)
    private Instant receivedAt;
}
