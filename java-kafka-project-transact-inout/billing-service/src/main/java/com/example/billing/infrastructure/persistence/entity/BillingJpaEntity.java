package com.example.billing.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "billing")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class BillingJpaEntity {

    @Id
    @Column(nullable = false, length = 255)
    private String id;

    @Column(nullable = false)
    private long amount;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
}
