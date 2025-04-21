package com.tdd.billing.entities;

import ch.qos.logback.core.net.server.Client;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "sales")
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // Relación Many-to-One con Cliente

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;  // Relación Many-to-One con Tienda

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SaleItem> items;  // Relación One-to-Many con Items de Venta

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;  // Monto total de la venta

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SaleStatus status = SaleStatus.COMPLETED;  // Estado de la venta

    @Column(name = "payment_method", nullable = false, length = 50)
    private String paymentMethod;  // Método de pago

    @Column(name = "sale_date", nullable = false, updatable = false)
    private LocalDateTime saleDate = LocalDateTime.now();  // Fecha de venta

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // Enum para los estados de venta
    public enum SaleStatus {
        PENDING, COMPLETED, CANCELLED, REFUNDED
    }
}
