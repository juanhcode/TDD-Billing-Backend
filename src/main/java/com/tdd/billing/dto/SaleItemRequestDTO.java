package com.tdd.billing.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SaleItemRequestDTO {
    private Long saleId;
    private Long productId;
    private Integer quantity;
    private BigDecimal unitPrice;
}
