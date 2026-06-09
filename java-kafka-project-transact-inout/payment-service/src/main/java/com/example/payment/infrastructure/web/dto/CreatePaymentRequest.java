package com.example.payment.infrastructure.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CreatePaymentRequest(
        @NotBlank String id,
        @Min(1) long amount
) {
}
