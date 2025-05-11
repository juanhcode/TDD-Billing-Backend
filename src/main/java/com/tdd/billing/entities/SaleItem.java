package com.tdd.billing.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "sale_items")
public class SaleItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "sale_id", nullable = false)
    private Sale sale;  // Relación Many-to-One con Venta

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;  // Relación Many-to-One con Producto

    @Column(nullable = false)
    private Integer quantity;  // Cantidad vendida

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;  // Precio unitario al momento de la venta

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;  // Cantidad * Precio unitario

    @Override
    public String toString() {
        return "SaleItem{" +
                "id=" + id +
                ", sale=" + sale +
                ", product=" + product +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", subtotal=" + subtotal +
                '}';
    }
}
