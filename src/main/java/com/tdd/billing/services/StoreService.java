package com.tdd.billing.services;
import com.tdd.billing.entities.Store;
import com.tdd.billing.repositories.StoreRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StoreService {
    private final StoreRepository storeRepository;

    public StoreService(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    public Store create(Store store) {
        return storeRepository.save(store);
    }

    public Optional<Store> findById(Long id) {
        return storeRepository.findById(id);
    }

    public Store update(Store store) {
        return storeRepository.save(store); // Si el ID ya existe, se actualiza; si no, se crea uno nuevo.
    }
}

