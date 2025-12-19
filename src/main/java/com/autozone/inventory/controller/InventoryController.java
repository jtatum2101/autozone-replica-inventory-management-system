package com.autozone.inventory.controller;

import com.autozone.inventory.entity.Inventory;
import com.autozone.inventory.entity.Part;
import com.autozone.inventory.entity.Store;
import com.autozone.inventory.service.InventoryService;
import com.autozone.inventory.service.PartService;
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
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
@Tag(name = "Inventory", description = "Inventory Management API")
public class InventoryController {

    private final InventoryService inventoryService;
    private final PartService partService;
    private final StoreService storeService;

    @GetMapping
    @Operation(summary = "Get all inventory")
    public ResponseEntity<List<Inventory>> getAllInventory() {
        return ResponseEntity.ok(inventoryService.getAllInventory());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get inventory by ID")
    public ResponseEntity<Inventory> getInventoryById(@PathVariable Long id) {
        return inventoryService.getInventoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/store/{storeId}")
    @Operation(summary = "Get inventory for a specific store")
    public ResponseEntity<List<Inventory>> getInventoryByStore(@PathVariable Long storeId) {
        return storeService.getStoreById(storeId)
                .map(store -> ResponseEntity.ok(inventoryService.getInventoryByStore(store)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/reorder")
    @Operation(summary = "Get all items needing reorder")
    public ResponseEntity<List<Inventory>> getItemsNeedingReorder() {
        return ResponseEntity.ok(inventoryService.getItemsNeedingReorder());
    }

    @GetMapping("/reorder/store/{storeId}")
    @Operation(summary = "Get items needing reorder for a specific store")
    public ResponseEntity<List<Inventory>> getItemsNeedingReorderByStore(@PathVariable Long storeId) {
        return storeService.getStoreById(storeId)
                .map(store -> ResponseEntity.ok(inventoryService.getItemsNeedingReorderByStore(store)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/low-stock")
    @Operation(summary = "Get low stock items")
    public ResponseEntity<List<Inventory>> getLowStockItems() {
        return ResponseEntity.ok(inventoryService.getLowStockItems());
    }

    @PostMapping
    @Operation(summary = "Create or update inventory")
    public ResponseEntity<Inventory> createInventory(@Valid @RequestBody Inventory inventory) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(inventoryService.createOrUpdateInventory(inventory));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete inventory")
    public ResponseEntity<Void> deleteInventory(@PathVariable Long id) {
        inventoryService.deleteInventory(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/calculate-reorder/{inventoryId}")
    @Operation(summary = "Calculate optimal reorder point for an inventory item")
    public ResponseEntity<Integer> calculateReorderPoint(@PathVariable Long inventoryId) {
        return inventoryService.getInventoryById(inventoryId)
                .map(inventory -> {
                    Integer optimalReorderPoint = inventoryService.calculateOptimalReorderPoint(
                            inventory.getPart(),
                            inventory.getStore()
                    );
                    return ResponseEntity.ok(optimalReorderPoint);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/update-all-reorder-points")
    @Operation(summary = "Update reorder points for all inventory based on sales trends")
    public ResponseEntity<String> updateAllReorderPoints() {
        inventoryService.updateAllReorderPoints();
        return ResponseEntity.ok("All reorder points updated successfully");
    }
}