package com.autozone.inventory.repository;

import com.autozone.inventory.entity.Inventory;
import com.autozone.inventory.entity.Part;
import com.autozone.inventory.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    Optional<Inventory> findByPartAndStore(Part part, Store store);

    List<Inventory> findByStore(Store store);

    List<Inventory> findByPart(Part part);

    // Find items that need reordering (quantity <= reorderPoint)
    @Query("SELECT i FROM Inventory i WHERE i.quantity <= i.reorderPoint AND i.deleted = false")
    List<Inventory> findItemsNeedingReorder();

    // Find items that need reordering for a specific store
    @Query("SELECT i FROM Inventory i WHERE i.store = :store AND i.quantity <= i.reorderPoint AND i.deleted = false")
    List<Inventory> findItemsNeedingReorderByStore(Store store);

    // Find low stock items (below 20% of max)
    @Query("SELECT i FROM Inventory i WHERE i.quantity < (i.maxStockLevel * 0.2) AND i.deleted = false")
    List<Inventory> findLowStockItems();
}