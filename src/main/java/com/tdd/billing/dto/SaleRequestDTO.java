package com.tdd.billing.dto;

import com.tdd.billing.entities.Sale;
import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SaleRequestDTO {
    private Long userId;
    private Long storeId;
    private String paymentMethod;
    private BigDecimal totalAmount;
    private Sale.SaleStatus status;
}
