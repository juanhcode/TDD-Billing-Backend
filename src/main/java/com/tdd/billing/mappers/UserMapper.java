package com.tdd.billing.mappers;

import com.tdd.billing.dto.UpdateUserDTO;
import com.tdd.billing.dto.UserResponseDTO;
import com.tdd.billing.entities.User;
import com.tdd.billing.entities.UserRole;
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
    public static void updateUserFromDTO(UpdateUserDTO dto, User user) {
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setRole(UserRole.valueOf(dto.getRole()));
        user.setStatus(dto.isStatus());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setAddress(dto.getAddress());
    }

}
