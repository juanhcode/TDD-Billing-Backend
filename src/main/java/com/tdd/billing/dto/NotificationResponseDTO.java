package com.tdd.billing.dto;


import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class NotificationResponseDTO {
    private Long id;
    private Long userId;
    private Long productId;
    private String title;
    private String message;
    private String type;
    private Boolean isRead;
    private LocalDateTime createdAt;

}

