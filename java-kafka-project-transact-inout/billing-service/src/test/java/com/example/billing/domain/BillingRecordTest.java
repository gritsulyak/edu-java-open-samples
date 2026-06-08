package com.example.billing.domain;

import com.example.billing.domain.model.BillingRecord;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

@DisplayName("BillingRecord — domain model")
class BillingRecordTest {

    @Test
    @DisplayName("create() produces valid record")
    void create_valid() {
        BillingRecord r = BillingRecord.create("bill-1", 250L);
        assertThat(r.id()).isEqualTo("bill-1");
        assertThat(r.amount()).isEqualTo(250L);
        assertThat(r.createdAt()).isNotNull();
    }

    @ParameterizedTest(name = "blank id [{0}] should throw")
    @ValueSource(strings = {"", " "})
    @DisplayName("create() rejects blank id")
    void create_blankId_throws(String id) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> BillingRecord.create(id, 100L));
    }

    @Test
    @DisplayName("create() rejects non-positive amount")
    void create_nonPositiveAmount_throws() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> BillingRecord.create("bill-1", 0L));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> BillingRecord.create("bill-1", -99L));
    }
}
