package com.tdd.billing.mappers;

import com.tdd.billing.dto.StoreRequestDTO;
import com.tdd.billing.dto.StoreResponseDTO;
import com.tdd.billing.entities.Store;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class StoreMapperTest {

    @Test
    void toDTO_shouldMapAllFieldsCorrectly() {
        Store store = new Store();
        store.setId(10L);
        store.setName("Tienda Ejemplo");
        store.setUrl("http://tienda.ejemplo");
        store.setEmail("contacto@tienda.com");
        store.setContact("123456789");
        store.setNit("900123456");
        store.setLogo("http://tienda.ejemplo/logo.png");
        store.setDescription("Descripción de la tienda");
        store.setStatus(true);
        store.setAddress("Calle Principal 123");
        store.setCreatedAt(LocalDateTime.parse("2024-05-01T10:30:00")); // asumiendo String

        StoreResponseDTO dto = StoreMapper.toDTO(store);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(store.getId());
        assertThat(dto.getName()).isEqualTo(store.getName());
        assertThat(dto.getUrl()).isEqualTo(store.getUrl());
        assertThat(dto.getEmail()).isEqualTo(store.getEmail());
        assertThat(dto.getContact()).isEqualTo(store.getContact());
        assertThat(dto.getNit()).isEqualTo(store.getNit());
        assertThat(dto.getLogo()).isEqualTo(store.getLogo());
        assertThat(dto.getDescription()).isEqualTo(store.getDescription());
        assertThat(dto.getStatus()).isEqualTo(store.getStatus());
        assertThat(dto.getAddress()).isEqualTo(store.getAddress());
        assertThat(dto.getCreatedAt()).isEqualTo(store.getCreatedAt());
    }

    @Test
    void toEntity_shouldMapAllFieldsCorrectly() {
        StoreRequestDTO dto = new StoreRequestDTO();
        dto.setName("Tienda DTO");
        dto.setUrl("http://tienda.dto");
        dto.setEmail("dto@tienda.com");
        dto.setContact("987654321");
        dto.setNit("800987654");
        dto.setDescription("Descripción DTO");
        dto.setStatus(false);
        dto.setAddress("Avenida Secundaria 456");

        Store store = StoreMapper.toEntity(dto);

        assertThat(store).isNotNull();
        assertThat(store.getName()).isEqualTo(dto.getName());
        assertThat(store.getUrl()).isEqualTo(dto.getUrl());
        assertThat(store.getEmail()).isEqualTo(dto.getEmail());
        assertThat(store.getContact()).isEqualTo(dto.getContact());
        assertThat(store.getNit()).isEqualTo(dto.getNit());
        assertThat(store.getDescription()).isEqualTo(dto.getDescription());
        assertThat(store.getStatus()).isEqualTo(dto.getStatus());
        assertThat(store.getAddress()).isEqualTo(dto.getAddress());
    }
}
