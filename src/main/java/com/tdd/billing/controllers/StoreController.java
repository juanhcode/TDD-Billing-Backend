package com.tdd.billing.controllers;
import com.tdd.billing.entities.Store;
import com.tdd.billing.services.StoreService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/stores")
public class StoreController {

    private final StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @PostMapping
    public ResponseEntity<?> register(@RequestBody Store store) {
        try {
            Store createdStore = storeService.create(store);
            return ResponseEntity.ok(createdStore);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error creating store: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Store store) {
        try {
            Optional<Store> existingStore = storeService.findById(id);
            if (existingStore.isPresent()) {
                store.setId(id);

                // Aquí, si el userId no está presente en la tienda proporcionada, busca el usuario por ID
                if (store.getUserId() == null || store.getUserId().getId() == null) {
                    store.setUserId(existingStore.get().getUserId()); // Mantén el userId actual si no se proporciona uno nuevo
                }

                Store updatedStore = storeService.update(store);
                return ResponseEntity.ok(updatedStore);
            } else {
                return ResponseEntity.status(404).body("Store not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error updating store: " + e.getMessage());
        }
    }

}



