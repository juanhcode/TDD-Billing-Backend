package com.tdd.billing.entities;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class CategoryTest {

    @Test
    void shouldSetAndGetAllFieldsCorrectly() {
        Store mockStore = mock(Store.class);
        LocalDateTime now = LocalDateTime.now();

        Category category = new Category();
        category.setId(1L);
        category.setStore(mockStore);
        category.setName("Electronics");
        category.setDescription("Electronic gadgets and devices");
        category.setStatus(false);
        category.setCreatedAt(now);

        assertThat(category.getId()).isEqualTo(1L);
        assertThat(category.getStore()).isEqualTo(mockStore);
        assertThat(category.getName()).isEqualTo("Electronics");
        assertThat(category.getDescription()).isEqualTo("Electronic gadgets and devices");
        assertThat(category.getStatus()).isFalse();
        assertThat(category.getCreatedAt()).isEqualTo(now);
    }

    @Test
    void allArgsConstructorShouldSetValuesCorrectly() {
        Store mockStore = mock(Store.class);
        LocalDateTime now = LocalDateTime.now();

        Category category = new Category(
                5L,
                mockStore,
                "Books",
                "Books and novels",
                true,
                now
        );

        assertThat(category.getId()).isEqualTo(5L);
        assertThat(category.getStore()).isEqualTo(mockStore);
        assertThat(category.getName()).isEqualTo("Books");
        assertThat(category.getDescription()).isEqualTo("Books and novels");
        assertThat(category.getStatus()).isTrue();
        assertThat(category.getCreatedAt()).isEqualTo(now);
    }

    @Test
    void constructorWithOnlyIdShouldSetIdCorrectly() {
        Category category = new Category(99L);

        assertThat(category.getId()).isEqualTo(99L);
        assertThat(category.getStore()).isNull();
        assertThat(category.getName()).isNull();
        assertThat(category.getDescription()).isNull();
    }

    @Test
    void onCreateShouldSetCreatedAtIfNull() {
        Category category = new Category();
        category.setCreatedAt(null);

        category.onCreate();

        assertThat(category.getCreatedAt()).isNotNull();
    }

    @Test
    void onCreateShouldNotOverrideCreatedAtIfAlreadySet() {
        Category category = new Category();
        LocalDateTime existing = LocalDateTime.of(2024, 1, 1, 10, 0);
        category.setCreatedAt(existing);

        category.onCreate();

        assertThat(category.getCreatedAt()).isEqualTo(existing);
    }
}
