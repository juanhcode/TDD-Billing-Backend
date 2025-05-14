package com.tdd.billing.utils;

import com.tdd.billing.dto.UserResponseDTO;
import com.tdd.billing.entities.User;

public class UserMapper {

    public static UserResponseDTO toDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole().name(),
                user.getStatus(),
                user.getPhotoUrl(),
                user.getPhoneNumber(),
                user.getAddress(),
                user.getCreatedAt().toString()
        );
    }
}
