package com.tdd.billing.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private Boolean status;
    private String photoUrl;
    private String phoneNumber;
    private String address;
    private String createdAt;
}

