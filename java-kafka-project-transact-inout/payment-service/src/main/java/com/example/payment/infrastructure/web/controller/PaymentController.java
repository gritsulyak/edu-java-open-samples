package com.example.payment.infrastructure.web.controller;

import com.example.payment.domain.model.Payment;
import com.example.payment.domain.port.in.CreatePaymentUseCase;
import com.example.payment.infrastructure.web.dto.CreatePaymentRequest;
import com.example.payment.infrastructure.web.dto.PaymentResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final CreatePaymentUseCase createPaymentUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentResponse create(@Valid @RequestBody CreatePaymentRequest request) {
        Payment payment = createPaymentUseCase.execute(request.id(), request.amount());
        return PaymentResponse.from(payment);
    }
}
