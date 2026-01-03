package com.autozone.inventory.service;

import com.autozone.inventory.entity.Inventory;
import com.autozone.inventory.entity.Part;
import com.autozone.inventory.entity.Store;
import com.autozone.inventory.repository.InventoryRepository;
import com.autozone.inventory.repository.SaleRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    private final SaleRepository saleRepository;

    public List<Inventory> getAllInventory(){
        return inventoryRepository.findAll();
    }

    public Optional<Inventory> getInventoryById(Long id){
        return inventoryRepository.findById(id);
    }

    public Optional<Inventory> getInventoryByPartAndStore(Part part, Store store){
        return inventoryRepository.findByPartAndStore(part, store);
    }

    public List<Inventory> getInventoryByStore(Store store){
        return inventoryRepository.findByStore(store);
    }

    public List<Inventory> getItemsNeedingReorder(){
        return inventoryRepository.findItemsNeedingReorder();
    }

    public List<Inventory> getItemsNeedingReorderByStore(Store store){
        return inventoryRepository.findItemsNeedingReorderByStore(store);
    }

    public List<Inventory> getLowStockItems(){
        return inventoryRepository.findLowStockItems();
    }

    public Inventory createOrUpdateInventory(Inventory inventory){
        return inventoryRepository.save(inventory);
    }

    public void deleteInventory(Long id){
        inventoryRepository.findById(id).ifPresent(inventory->{
            inventory.setDeleted(true);
            inventoryRepository.save(inventory);
        });
    }

    /**
     * Intelligent reorder point calculation based on sales velocity
     * Uses 30-day, 60-day, and 90-day sales trends
     */

    public Integer calculateOptimalReorderPoint(Part part, Store store){
        LocalDateTime now = LocalDateTime.now();

        //Get sales data for different periods

        Integer sold30 = saleRepository.getTotalQuantitySold(part.getId(), store.getId(), now.minusDays(30), now);
        Integer sold60 = saleRepository.getTotalQuantitySold(part.getId(), store.getId(), now.minusDays(60), now);
        Integer sold90 = saleRepository.getTotalQuantitySold(part.getId(), store.getId(), now.minusDays(90), now);

        // Calculate average daily sales with weighted average (recent data weighted more)

        double dailyVelocity30 = sold30 != null ? sold30 / 30.0 : 0;
        double dailyVelocity60 = sold60 != null ? sold60 / 60.0 : 0;
        double dailyVelocity90 = sold90 != null ? sold90 / 90.0 : 0;

        //Weighted average: 50% weight on last 30 days, 30% on 60 days, 20% on 90 days

        double weightedDailySales = (dailyVelocity30 * 0.5) + (dailyVelocity60 * 0.3) + (dailyVelocity90 * 0.2);

        //Calculate reorder point: (average daily sales * lead time) + safety stock

        int leadTimeDays = part.getSupplierLeadTimeDays();
        int safetyStockDays = 7; //Extra week of buffer

        int reorderPoint = (int) Math.ceil(weightedDailySales * (leadTimeDays + safetyStockDays));

        return Math.max(reorderPoint, 5);
    }

    /**
     * Calculate optimal reorder quantity based on sales velocity and max stock
     */

    public Integer calculateOptimalReorderQuantity(Part part, Store store, Inventory inventory) {
        LocalDateTime now = LocalDateTime.now();

        // Get 30-day sales trend
        Integer sold30Days = saleRepository.getTotalQuantitySold(part.getId(), store.getId(), now.minusDays(30), now);
        double dailyVelocity = sold30Days != null ? sold30Days / 30.0 : 0;

        // Order enough to last 30-45 days
        int targetDays = 30;
        int optimalQuantity = (int) Math.ceil(dailyVelocity * targetDays);

        // Don't exceed max stock level
        int currentStock = inventory.getQuantity();
        int maxOrder = inventory.getMaxStockLevel() - currentStock;

        // Return the minimum of optimal quantity and what fits in max stock
        return Math.min(optimalQuantity, maxOrder);
    }

    /**
     * Update reorder points for all inventory items based on sales trends
     */

    public void updateAllReorderPoints(){
        List<Inventory> allInventory = inventoryRepository.findAll();
        for (Inventory inventory : allInventory){
            Integer optimalReorderPoint = calculateOptimalReorderPoint(
                    inventory.getPart(),
                    inventory.getStore()
            );

            inventory.setReorderPoint(optimalReorderPoint);
            inventoryRepository.save(inventory);
            log.info("Updated reorder point for Part: {} at Store: {} to {}",
                    inventory.getPart().getSku(),
                    inventory.getStore().getStoreNumber(),
                    optimalReorderPoint);
        }
    }

    /**
     * process a sale - decrements inventory and reorders the sale}
     */

    public void processSale(Part part, Store store, Integer quantity){
        Optional<Inventory> inventoryOpt = inventoryRepository.findByPartAndStore(part, store);

        if(inventoryOpt.isPresent()){
            Inventory inventory = inventoryOpt.get();
            int newQuantity = inventory.getQuantity() - quantity;

            if (newQuantity < 0){
                throw new IllegalStateException("Insufficient inventory for sale");
            }

            inventory.setQuantity(newQuantity);
            inventoryRepository.save(inventory);

            log.info("Processed sale: {} units of {} at Store {}. New inventory: {}",
                    quantity,
                    part.getSku(),
                    store.getStoreNumber(),
                    newQuantity);

            if (newQuantity <= inventory.getReorderQuantity()){
                log.warn("REORDER ALERT: Part {} at Store {} is below reorder point!",
                        part.getSku(),
                        store.getStoreNumber());
            }
        }
    }

}
