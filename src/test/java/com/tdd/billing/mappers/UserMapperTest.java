package com.tdd.billing.mappers;

import com.tdd.billing.dto.UpdateUserDTO;
import com.tdd.billing.dto.UserResponseDTO;
import com.tdd.billing.entities.User;
import com.tdd.billing.entities.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class UserMapperTest {

    @Test
    void toDTO_shouldMapAllFieldsCorrectly() {
        User user = new User();
        user.setId(1L);
        user.setFirstName("Juan");
        user.setLastName("Pérez");
        user.setEmail("juan@example.com");
        user.setRole(UserRole.ADMIN);
        user.setStatus(true);
        user.setPhotoUrl("http://photo.url/juan.jpg");
        user.setPhoneNumber("123456789");
        user.setAddress("Calle Falsa 123");
        user.setCreatedAt(LocalDateTime.of(2024, 5, 1, 10, 30));

        UserResponseDTO dto = UserMapper.toDTO(user);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(user.getId());
        assertThat(dto.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(dto.getLastName()).isEqualTo(user.getLastName());
        assertThat(dto.getEmail()).isEqualTo(user.getEmail());
        assertThat(dto.getRole()).isEqualTo(user.getRole().name());
        assertThat(dto.getStatus()).isEqualTo(user.getStatus());
        assertThat(dto.getPhotoUrl()).isEqualTo(user.getPhotoUrl());
        assertThat(dto.getPhoneNumber()).isEqualTo(user.getPhoneNumber());
        assertThat(dto.getAddress()).isEqualTo(user.getAddress());
        assertThat(dto.getCreatedAt()).isEqualTo(user.getCreatedAt().toString());
    }

    @Test
    void updateUserFromDTO_shouldUpdateUserFieldsCorrectly() {
        User user = new User();
        user.setFirstName("OldName");
        user.setLastName("OldLastName");
        user.setEmail("old@example.com");
        user.setRole(UserRole.ADMIN);
        user.setStatus(false);
        user.setPhoneNumber("000000000");
        user.setAddress("Old Address");

        UpdateUserDTO dto = new UpdateUserDTO();
        dto.setFirstName("NewName");
        dto.setLastName("NewLastName");
        dto.setEmail("new@example.com");
        dto.setRole("ADMIN");
        dto.setStatus(true);
        dto.setPhoneNumber("999999999");
        dto.setAddress("New Address");

        UserMapper.updateUserFromDTO(dto, user);

        assertThat(user.getFirstName()).isEqualTo(dto.getFirstName());
        assertThat(user.getLastName()).isEqualTo(dto.getLastName());
        assertThat(user.getEmail()).isEqualTo(dto.getEmail());
        assertThat(user.getRole()).isEqualTo(UserRole.ADMIN);
        assertThat(user.getStatus()).isEqualTo(dto.isStatus());
        assertThat(user.getPhoneNumber()).isEqualTo(dto.getPhoneNumber());
        assertThat(user.getAddress()).isEqualTo(dto.getAddress());
    }
}
