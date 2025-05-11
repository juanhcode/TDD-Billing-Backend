// Request DTO
package com.tdd.billing.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class InvoiceRequestDTO {
    private Long saleId;
    private Long storeId;
    private BigDecimal subtotal;
    private BigDecimal taxes;
    private BigDecimal total;
    private LocalDateTime issueDate;
    private String invoiceNumber;
    private String paymentMethod;
    private String status;
}
