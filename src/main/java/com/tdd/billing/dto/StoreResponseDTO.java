package com.tdd.billing.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class StoreResponseDTO {
    private Long id;
    private String name;
    private String url;
    private String email;
    private String contact;
    private String nit;
    private String logo;
    private String description;
    private Boolean status;
    private String address;
    private LocalDateTime createdAt;

    public StoreResponseDTO() {
    }
}
