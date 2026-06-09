package com.example.payment.infrastructure.web;

import com.example.payment.domain.model.Payment;
import com.example.payment.domain.port.in.CreatePaymentUseCase;
import com.example.payment.infrastructure.web.controller.PaymentController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PaymentController.class)
@DisplayName("PaymentController — WebMvc slice")
class PaymentControllerTest {

    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper om;

    @MockBean
    CreatePaymentUseCase createPaymentUseCase;

    @Test
    @DisplayName("POST /payments → 201 with body")
    void createPayment_valid_returns201() throws Exception {
        when(createPaymentUseCase.execute(eq("pay-1"), eq(500L)))
                .thenReturn(new Payment("pay-1", 500L, Instant.parse("2024-01-01T00:00:00Z")));

        mvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"id":"pay-1","amount":500}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("pay-1"))
                .andExpect(jsonPath("$.amount").value(500));
    }

    @Test
    @DisplayName("POST /payments with zero amount → 400")
    void createPayment_zeroAmount_returns400() throws Exception {
        mvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"id":"pay-1","amount":0}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /payments with blank id → 400")
    void createPayment_blankId_returns400() throws Exception {
        mvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"id":"","amount":100}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /payments with missing body → 400")
    void createPayment_missingBody_returns400() throws Exception {
        mvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
