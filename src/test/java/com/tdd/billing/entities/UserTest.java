package com.tdd.billing.entities;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserTest {

    @Test
    void shouldSetAndGetAllFieldsCorrectly() {
        Store mockStore = mock(Store.class);

        LocalDateTime now = LocalDateTime.now();

        User user = new User();
        user.setId(1L);
        user.setFirstName("Ana");
        user.setLastName("Pérez");
        user.setEmail("ana.perez@example.com");
        user.setPassword("secret123");
        user.setRole(UserRole.ADMIN);
        user.setPhotoUrl("http://example.com/photo.jpg");
        user.setStatus(false);
        user.setPhoneNumber("123456789");
        user.setCreatedAt(now);
        user.setAddress("Calle 123");
        user.setStore(mockStore);

        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getFirstName()).isEqualTo("Ana");
        assertThat(user.getLastName()).isEqualTo("Pérez");
        assertThat(user.getEmail()).isEqualTo("ana.perez@example.com");
        assertThat(user.getPassword()).isEqualTo("secret123");
        assertThat(user.getRole()).isEqualTo(UserRole.ADMIN);
        assertThat(user.getPhotoUrl()).isEqualTo("http://example.com/photo.jpg");
        assertThat(user.getStatus()).isFalse();
        assertThat(user.getPhoneNumber()).isEqualTo("123456789");
        assertThat(user.getCreatedAt()).isEqualTo(now);
        assertThat(user.getAddress()).isEqualTo("Calle 123");
        assertThat(user.getStore()).isEqualTo(mockStore);
    }

    @Test
    void allArgsConstructorShouldSetValuesCorrectly() {
        LocalDateTime createdAt = LocalDateTime.now();
        Store store = new Store();
        store.setId(99L);

        User user = new User(
                10L,
                "Luis",
                "Ramírez",
                "luis.ramirez@example.com",
                "pwd123",
                UserRole.CUSTOMER,
                "http://img.com/photo.png",
                true,
                "0987654321",
                createdAt,
                "Avenida Siempre Viva 742",
                store
        );

        assertThat(user.getId()).isEqualTo(10L);
        assertThat(user.getFirstName()).isEqualTo("Luis");
        assertThat(user.getLastName()).isEqualTo("Ramírez");
        assertThat(user.getEmail()).isEqualTo("luis.ramirez@example.com");
        assertThat(user.getPassword()).isEqualTo("pwd123");
        assertThat(user.getRole()).isEqualTo(UserRole.CUSTOMER);
        assertThat(user.getPhotoUrl()).isEqualTo("http://img.com/photo.png");
        assertThat(user.getStatus()).isTrue();
        assertThat(user.getPhoneNumber()).isEqualTo("0987654321");
        assertThat(user.getCreatedAt()).isEqualTo(createdAt);
        assertThat(user.getAddress()).isEqualTo("Avenida Siempre Viva 742");
        assertThat(user.getStore()).isEqualTo(store);
    }

    @Test
    void toStringShouldIncludeExpectedFields() {
        Store store = new Store();
        store.setId(5L);

        User user = new User(
                2L,
                "Mario",
                "Lopez",
                "mario.lopez@example.com",
                "1234",
                UserRole.CUSTOMER,
                null,
                true,
                "555000123",
                LocalDateTime.of(2025, 6, 1, 10, 30),
                null,
                store
        );

        String toString = user.toString();

        assertThat(toString).contains("id=2");
        assertThat(toString).contains("firstName='Mario'");
        assertThat(toString).contains("role=CUSTOMER");
        assertThat(toString).contains("store=5");
    }
}
