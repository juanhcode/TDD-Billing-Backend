package com.tdd.billing.entities;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class SaleItemTest {

    @Test
    void shouldSetAndGetAllFieldsCorrectly() {
        Sale mockSale = mock(Sale.class);
        Product mockProduct = mock(Product.class);

        SaleItem item = new SaleItem();
        item.setId(1L);
        item.setSale(mockSale);
        item.setProduct(mockProduct);
        item.setQuantity(3);
        item.setUnitPrice(new BigDecimal("25.50"));
        item.setSubtotal(new BigDecimal("76.50"));

        assertThat(item.getId()).isEqualTo(1L);
        assertThat(item.getSale()).isEqualTo(mockSale);
        assertThat(item.getProduct()).isEqualTo(mockProduct);
        assertThat(item.getQuantity()).isEqualTo(3);
        assertThat(item.getUnitPrice()).isEqualByComparingTo("25.50");
        assertThat(item.getSubtotal()).isEqualByComparingTo("76.50");
    }

    @Test
    void allArgsConstructorShouldSetValuesCorrectly() {
        Sale mockSale = mock(Sale.class);
        Product mockProduct = mock(Product.class);
        SaleItem item = new SaleItem(
                10L,
                mockSale,
                mockProduct,
                2,
                new BigDecimal("30.00"),
                new BigDecimal("60.00")
        );

        assertThat(item.getId()).isEqualTo(10L);
        assertThat(item.getSale()).isEqualTo(mockSale);
        assertThat(item.getProduct()).isEqualTo(mockProduct);
        assertThat(item.getQuantity()).isEqualTo(2);
        assertThat(item.getUnitPrice()).isEqualByComparingTo("30.00");
        assertThat(item.getSubtotal()).isEqualByComparingTo("60.00");
    }

    @Test
    void toStringShouldIncludeAllFields() {
        Sale mockSale = mock(Sale.class);
        Product mockProduct = mock(Product.class);
        SaleItem item = new SaleItem();
        item.setId(5L);
        item.setSale(mockSale);
        item.setProduct(mockProduct);
        item.setQuantity(1);
        item.setUnitPrice(new BigDecimal("50.00"));
        item.setSubtotal(new BigDecimal("50.00"));

        String result = item.toString();

        assertThat(result).contains("SaleItem{");
        assertThat(result).contains("id=5");
        assertThat(result).contains("quantity=1");
        assertThat(result).contains("unitPrice=50.00");
        assertThat(result).contains("subtotal=50.00");
    }
}
