package com.tdd.billing.mappers;
import com.tdd.billing.dto.SaleResponseDto;
import com.tdd.billing.entities.Sale;

import java.util.List;
import java.util.stream.Collectors;

public class SaleMapper {

    public static SaleResponseDto toDto(Sale sale) {
        return SaleResponseDto.builder()
                .id(sale.getId())
                .storeId(sale.getStore().getId())
                .userId(sale.getUser().getId())
                .saleDate(sale.getSaleDate())
                .paymentMethod(sale.getPaymentMethod())
                .totalAmount(sale.getTotalAmount())
                .status(sale.getStatus().name())
                .deleted(sale.isDeleted())
                .createdAt(sale.getCreatedAt())
                .build();
    }

    public static List<SaleResponseDto> toDtoList(List<Sale> sales) {
        return sales.stream()
                .map(SaleMapper::toDto)
                .collect(Collectors.toList());
    }
}
