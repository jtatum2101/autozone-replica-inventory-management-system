package com.autozone.inventory.controller;

import com.autozone.inventory.entity.Store;
import com.autozone.inventory.service.StoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/stores")
@RequiredArgsConstructor
@Tag(name = "Stores", description = "Store Management API")
public class StoreController {

    private final StoreService storeService;

    @GetMapping
    @Operation(summary = "Get All stores")
    public ResponseEntity<List<Store>> getAllStores(){
        return ResponseEntity.ok(storeService.getAllStores());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get store by ID")
    public ResponseEntity<Store> getStoreById(@PathVariable Long id){
        return storeService.getStoreById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/number/{storeNumber}")
    @Operation(summary = "Get store by store number")
    public ResponseEntity<Store> getStoreByNumber(@PathVariable String storeNumber){
        return storeService.getStoreByStoreNumber(storeNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a new store")
    public ResponseEntity<Store> createStore(@Valid @RequestBody Store store){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(storeService.createStore(store));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a store")
    public ResponseEntity<Store> updateStore(
            @PathVariable Long id,
            @Valid @RequestBody Store store) {
        return ResponseEntity.ok(storeService.updateStore(id, store));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a store")
    public ResponseEntity<Void> deleteStore(@PathVariable Long id){
        storeService.deleteStore(id);
        return ResponseEntity.noContent().build();
    }
}
