package com.tdd.billing.entities;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class ProductTest {

    @Test
    void shouldCreateProductWithDefaultValues() {
        Product product = new Product();

        assertThat(product.getStock()).isEqualTo(0);
        assertThat(product.getRatingRate()).isEqualTo(BigDecimal.ZERO);
        assertThat(product.getRatingCount()).isEqualTo(0);
        assertThat(product.getStatus()).isTrue();
        assertThat(product.getCreatedAt()).isNotNull();
    }

    @Test
    void shouldSetAndGetAllFieldsCorrectly() {
        Store mockStore = mock(Store.class);
        Category mockCategory = mock(Category.class);
        User mockUser = mock(User.class);
        LocalDateTime now = LocalDateTime.now();

        Product product = new Product();
        product.setId(1L);
        product.setStore(mockStore);
        product.setCategory(mockCategory);
        product.setUser(mockUser);
        product.setName("Zapatos deportivos");
        product.setDescription("Cómodos para correr");
        product.setPrice(new BigDecimal("99.99"));
        product.setStock(10);
        product.setUrl("http://example.com/product.png");
        product.setRatingRate(new BigDecimal("4.5"));
        product.setRatingCount(123);
        product.setStatus(false);
        product.setCreatedAt(now);

        assertThat(product.getId()).isEqualTo(1L);
        assertThat(product.getStore()).isEqualTo(mockStore);
        assertThat(product.getCategory()).isEqualTo(mockCategory);
        assertThat(product.getUser()).isEqualTo(mockUser);
        assertThat(product.getName()).isEqualTo("Zapatos deportivos");
        assertThat(product.getDescription()).isEqualTo("Cómodos para correr");
        assertThat(product.getPrice()).isEqualByComparingTo("99.99");
        assertThat(product.getStock()).isEqualTo(10);
        assertThat(product.getUrl()).isEqualTo("http://example.com/product.png");
        assertThat(product.getRatingRate()).isEqualByComparingTo("4.5");
        assertThat(product.getRatingCount()).isEqualTo(123);
        assertThat(product.getStatus()).isFalse();
        assertThat(product.getCreatedAt()).isEqualTo(now);
    }

    @Test
    void onCreateShouldSetCreatedAtIfNull() {
        Product product = new Product();
        product.setCreatedAt(null);

        product.onCreate();

        assertThat(product.getCreatedAt()).isNotNull();
    }

    @Test
    void toStringShouldContainFieldValues() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");

        String result = product.toString();

        assertThat(result).contains("Product{");
        assertThat(result).contains("id=1");
        assertThat(result).contains("name='Test Product'");
    }
}
