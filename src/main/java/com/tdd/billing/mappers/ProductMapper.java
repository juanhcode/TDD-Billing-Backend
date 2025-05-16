package com.tdd.billing.mappers;

import com.tdd.billing.dto.ProductResponseDTO;
import com.tdd.billing.entities.Product;

public class ProductMapper {

    public static ProductResponseDTO toResponseDTO(Product product) {
        return new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getUrl(),
                product.getRatingRate(),
                product.getRatingCount(),
                product.getStatus()
        );
    }


    public static ProductResponseDTO toDTO(Product product) {
        return new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getUrl(),
                product.getRatingRate(),
                product.getRatingCount(),
                product.getStatus()
        );
    }
}
