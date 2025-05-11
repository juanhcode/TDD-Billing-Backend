package com.tdd.billing.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "invoices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sale_id", nullable = false)
    private Long saleId;

    @Column(name = "store_id", nullable = false)
    private Long storeId;

    @Column(nullable = false)
    private BigDecimal subtotal;

    @Column(nullable = false)
    private BigDecimal taxes;

    @Column(nullable = false)
    private BigDecimal total;

    @Column(name = "issue_date")
    private LocalDateTime issueDate;

    @Column(name = "invoice_number", nullable = false)
    private String invoiceNumber;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(nullable = false)
    private String status;
}
