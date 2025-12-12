package com.autozone.inventory.repository;

import com.autozone.inventory.entity.Part;
import com.autozone.inventory.entity.Sale;
import com.autozone.inventory.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

    List<Sale> findByStore(Store store);

    List<Sale> findByPart(Part part);

    List<Sale> findBySaleDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    // Get sales for a specific part at a specific store in a date range
    List<Sale> findByPartAndStoreAndSaleDateBetween(
            Part part,
            Store store,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    // Calculate total quantity sold for a part in the last N days
    @Query("SELECT COALESCE(SUM(s.quantitySold), 0) FROM Sale s " +
            "WHERE s.part = :part AND s.store = :store " +
            "AND s.saleDate >= :startDate")
    Integer getTotalQuantitySold(Part part, Store store, LocalDateTime startDate);

    // Get top selling parts for a store
    @Query("SELECT s.part, SUM(s.quantitySold) as total FROM Sale s " +
            "WHERE s.store = :store AND s.saleDate >= :startDate " +
            "GROUP BY s.part ORDER BY total DESC")
    List<Object[]> getTopSellingParts(Store store, LocalDateTime startDate);
}