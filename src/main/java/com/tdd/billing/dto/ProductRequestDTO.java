package com.tdd.billing.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDTO {
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private BigDecimal ratingRate;
    private Integer ratingCount;
    private Boolean status;
    private Long storeId;
    private Long categoryId;
    private Long userId;
    private LocalDateTime createdAt;
}
