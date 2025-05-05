package com.tdd.billing.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
    private Long id;
    private String name;
    private String description;
    private Boolean status;
    private Long storeId;
    private LocalDateTime createdAt;
}
