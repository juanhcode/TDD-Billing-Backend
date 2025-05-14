package com.tdd.billing.services;
import com.tdd.billing.dto.StoreResponseDTO;
import com.tdd.billing.entities.Store;
import com.tdd.billing.entities.User;
import com.tdd.billing.repositories.StoreRepository;
import com.tdd.billing.utils.StoreMapper;
import org.springframework.stereotype.Service;

import java.util.List;
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
        return storeRepository.save(store);
    }

    public Optional<StoreResponseDTO> getStoreDTOById(Long id) {
        return storeRepository.findById(id)
                .map(StoreMapper::toDTO);
    }
}
