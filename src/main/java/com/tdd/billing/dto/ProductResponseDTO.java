package com.tdd.billing.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductResponseDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private String url;
    private BigDecimal ratingRate;
    private Integer ratingCount;
    private Boolean status;

}
