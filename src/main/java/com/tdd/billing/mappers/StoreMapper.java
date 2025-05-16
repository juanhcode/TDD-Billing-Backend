package com.tdd.billing.mappers;

import com.tdd.billing.dto.StoreRequestDTO;
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

    public static Store toEntity(StoreRequestDTO dto) {
        Store store = new Store();
        store.setName(dto.getName());
        store.setUrl(dto.getUrl());
        store.setEmail(dto.getEmail());
        store.setContact(dto.getContact());
        store.setNit(dto.getNit());
        store.setDescription(dto.getDescription());
        store.setStatus(dto.getStatus());
        store.setAddress(dto.getAddress());
        return store;
    }
}
