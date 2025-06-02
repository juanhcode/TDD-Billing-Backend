package com.tdd.billing.entities;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class InvoiceTest {

    @Test
    void shouldSetAndGetAllFieldsCorrectly() {
        LocalDateTime issueDate = LocalDateTime.now();

        Invoice invoice = new Invoice();
        invoice.setId(1L);
        invoice.setSaleId(10L);
        invoice.setStoreId(20L);
        invoice.setSubtotal(new BigDecimal("100.00"));
        invoice.setTaxes(new BigDecimal("19.00"));
        invoice.setTotal(new BigDecimal("119.00"));
        invoice.setIssueDate(issueDate);
        invoice.setInvoiceNumber("INV-2025-001");
        invoice.setPaymentMethod("CREDIT_CARD");
        invoice.setStatus("PAID");

        assertThat(invoice.getId()).isEqualTo(1L);
        assertThat(invoice.getSaleId()).isEqualTo(10L);
        assertThat(invoice.getStoreId()).isEqualTo(20L);
        assertThat(invoice.getSubtotal()).isEqualTo(new BigDecimal("100.00"));
        assertThat(invoice.getTaxes()).isEqualTo(new BigDecimal("19.00"));
        assertThat(invoice.getTotal()).isEqualTo(new BigDecimal("119.00"));
        assertThat(invoice.getIssueDate()).isEqualTo(issueDate);
        assertThat(invoice.getInvoiceNumber()).isEqualTo("INV-2025-001");
        assertThat(invoice.getPaymentMethod()).isEqualTo("CREDIT_CARD");
        assertThat(invoice.getStatus()).isEqualTo("PAID");
    }

    @Test
    void allArgsConstructorShouldSetValuesCorrectly() {
        LocalDateTime issueDate = LocalDateTime.now();

        Invoice invoice = new Invoice(
                2L,
                100L,
                200L,
                new BigDecimal("250.00"),
                new BigDecimal("47.50"),
                new BigDecimal("297.50"),
                issueDate,
                "INV-2025-002",
                "CASH",
                "PENDING"
        );

        assertThat(invoice.getId()).isEqualTo(2L);
        assertThat(invoice.getSaleId()).isEqualTo(100L);
        assertThat(invoice.getStoreId()).isEqualTo(200L);
        assertThat(invoice.getSubtotal()).isEqualTo(new BigDecimal("250.00"));
        assertThat(invoice.getTaxes()).isEqualTo(new BigDecimal("47.50"));
        assertThat(invoice.getTotal()).isEqualTo(new BigDecimal("297.50"));
        assertThat(invoice.getIssueDate()).isEqualTo(issueDate);
        assertThat(invoice.getInvoiceNumber()).isEqualTo("INV-2025-002");
        assertThat(invoice.getPaymentMethod()).isEqualTo("CASH");
        assertThat(invoice.getStatus()).isEqualTo("PENDING");
    }
}
