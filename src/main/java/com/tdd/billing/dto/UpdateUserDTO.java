// UpdateUserDTO.java
package com.tdd.billing.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String role;
    private boolean status;
    private String phoneNumber;
    private String address;
}
