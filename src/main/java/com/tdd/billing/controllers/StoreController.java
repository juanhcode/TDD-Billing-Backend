package com.tdd.billing.controllers;
import com.tdd.billing.dto.StoreRequestDTO;
import com.tdd.billing.dto.StoreResponseDTO;
import com.tdd.billing.entities.Store;
import com.tdd.billing.mappers.StoreMapper;
import com.tdd.billing.services.StoreService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Optional;

@RestController
@RequestMapping("/api/stores")
public class StoreController {

    private final StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @PostMapping
    public ResponseEntity<StoreResponseDTO> register(@RequestPart(name = "store") StoreRequestDTO storeDTO,
                                                     @RequestPart(name = "file", required = false) MultipartFile file) {
        try {
            Store createdStore = storeService.create(storeDTO, file);
            StoreResponseDTO responseDTO = StoreMapper.toDTO(createdStore);
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            throw new RuntimeException("Error creating store", e);
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Store store) {
        try {
            Optional<Store> existingStore = storeService.findById(id);
            if (existingStore.isPresent()) {
                store.setId(id);
                Store updatedStore = storeService.update(store);
                return ResponseEntity.ok(updatedStore);
            } else {
                return ResponseEntity.status(404).body("Store not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error updating store: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getStore(@PathVariable Long id) {
        try {
            Optional<StoreResponseDTO> storeDTO = storeService.getStoreDTOById(id);
            return storeDTO.<ResponseEntity<Object>>map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(404).body("Store not found"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error retrieving store: " + e.getMessage());
        }
    }
}



