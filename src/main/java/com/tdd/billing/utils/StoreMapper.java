package com.tdd.billing.utils;

import com.tdd.billing.dto.StoreResponseDTO;
import com.tdd.billing.entities.Store;

public class StoreMapper {

    public static StoreResponseDTO toDTO(Store store) {
        return new StoreResponseDTO(
                store.getId(),
                store.getName(),
                store.getUrl(),
                store.getEmail(),
                store.getContact(),
                store.getNit(),
                store.getLogo(),
                store.getDescription(),
                store.getStatus(),
                store.getAddress(),
                store.getCreatedAt()
        );
    }
}
