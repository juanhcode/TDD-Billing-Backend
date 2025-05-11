package com.tdd.billing.dto;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequestDTO {
    private Long userId;
    private Long productId;
    private String title;
    private String message;
    private String type;
}