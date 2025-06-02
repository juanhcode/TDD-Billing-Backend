package com.tdd.billing.mappers;

import com.tdd.billing.dto.ProductResponseDTO;
import com.tdd.billing.entities.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ProductMapperTest {

    @Test
    void toResponseDTO_shouldMapAllFieldsCorrectly() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Producto 1");
        product.setDescription("Descripción del producto");
        product.setPrice(BigDecimal.valueOf(100.0));
        product.setStock(50);
        product.setUrl("http://example.com/producto1");
        product.setRatingRate(BigDecimal.valueOf(4.5));
        product.setRatingCount(100);
        product.setStatus(true);

        ProductResponseDTO dto = ProductMapper.toResponseDTO(product);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(product.getId());
        assertThat(dto.getName()).isEqualTo(product.getName());
        assertThat(dto.getDescription()).isEqualTo(product.getDescription());
        assertThat(dto.getPrice()).isEqualTo(product.getPrice());
        assertThat(dto.getStock()).isEqualTo(product.getStock());
        assertThat(dto.getUrl()).isEqualTo(product.getUrl());
        assertThat(dto.getRatingRate()).isEqualTo(product.getRatingRate());
        assertThat(dto.getRatingCount()).isEqualTo(product.getRatingCount());
        assertThat(dto.getStatus()).isEqualTo(product.getStatus());
    }

    @Test
    void toDTO_shouldMapAllFieldsCorrectly() {
        Product product = new Product();
        product.setId(2L);
        product.setName("Producto 2");
        product.setDescription("Otra descripción");
        product.setPrice(BigDecimal.valueOf(200.0));
        product.setStock(20);
        product.setUrl("http://example.com/producto2");
        product.setRatingRate(BigDecimal.valueOf(3.8));
        product.setRatingCount(50);
        product.setStatus(false);

        ProductResponseDTO dto = ProductMapper.toDTO(product);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(product.getId());
        assertThat(dto.getName()).isEqualTo(product.getName());
        assertThat(dto.getDescription()).isEqualTo(product.getDescription());
        assertThat(dto.getPrice()).isEqualTo(product.getPrice());
        assertThat(dto.getStock()).isEqualTo(product.getStock());
        assertThat(dto.getUrl()).isEqualTo(product.getUrl());
        assertThat(dto.getRatingRate()).isEqualTo(product.getRatingRate());
        assertThat(dto.getRatingCount()).isEqualTo(product.getRatingCount());
        assertThat(dto.getStatus()).isEqualTo(product.getStatus());
    }
}
