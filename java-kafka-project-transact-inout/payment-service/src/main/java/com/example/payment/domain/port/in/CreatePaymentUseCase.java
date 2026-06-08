package com.example.payment.domain.port.in;

import com.example.payment.domain.model.Payment;

public interface CreatePaymentUseCase {
    Payment execute(String id, long amount);
}
