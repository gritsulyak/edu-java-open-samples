package com.example.payment.domain;

import com.example.payment.domain.model.Payment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Payment — domain model")
class PaymentTest {

    @Test
    @DisplayName("create() produces valid Payment with current timestamp")
    void create_validArgs_returnsPayment() {
        Payment p = Payment.create("pay-1", 500L);
        assertThat(p.id()).isEqualTo("pay-1");
        assertThat(p.amount()).isEqualTo(500L);
        assertThat(p.createdAt()).isNotNull();
    }

    @ParameterizedTest(name = "blank id [{0}] should throw")
    @ValueSource(strings = {"", " ", "\t"})
    @DisplayName("create() rejects blank id")
    void create_blankId_throws(String blankId) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> Payment.create(blankId, 100L))
                .withMessageContaining("id");
    }

    @Test
    @DisplayName("create() rejects zero amount")
    void create_zeroAmount_throws() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> Payment.create("pay-1", 0L))
                .withMessageContaining("amount");
    }

    @Test
    @DisplayName("create() rejects negative amount")
    void create_negativeAmount_throws() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> Payment.create("pay-1", -1L));
    }

    @Test
    @DisplayName("null id throws NullPointerException or IllegalArgumentException")
    void create_nullId_throws() {
        assertThatThrownBy(() -> Payment.create(null, 100L))
                .isInstanceOfAny(IllegalArgumentException.class, NullPointerException.class);
    }
}
