package com.tdd.billing.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SaleItemResponseDTO {
    private Long id;
    private Long saleId;
    private Long productId;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;
}
