package com.tdd.billing.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreRequestDTO {
    private String name;
    private String url;
    private String email;
    private String contact;
    private String nit;
    private String logo;
    private String description;
    private Boolean status;
    private String address;
}
