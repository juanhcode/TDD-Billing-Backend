package com.tdd.billing.services;
import com.tdd.billing.dto.StoreRequestDTO;
import com.tdd.billing.dto.StoreResponseDTO;
import com.tdd.billing.entities.Store;
import com.tdd.billing.repositories.StoreRepository;
import com.tdd.billing.mappers.StoreMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class StoreService {
    private final StoreRepository storeRepository;
    private final S3Service s3Service;

    public StoreService(StoreRepository storeRepository, S3Service s3Service) {
        this.storeRepository = storeRepository;
        this.s3Service = s3Service;
    }

    public Store create(StoreRequestDTO storeDTO, MultipartFile file) throws IOException {
        String photoUrl = (file != null && !file.isEmpty()) ? s3Service.uploadFile(file) : null;
        Store store = StoreMapper.toEntity(storeDTO);
        store.setLogo(photoUrl);
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
