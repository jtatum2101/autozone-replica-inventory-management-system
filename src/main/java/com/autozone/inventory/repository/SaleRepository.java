package com.autozone.inventory.repository;

import com.autozone.inventory.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

    // Find sales by store
    @Query("SELECT s FROM Sale s WHERE s.store.id = :storeId")
    List<Sale> findByStoreId(@Param("storeId") Long storeId);

    // Find sales by part
    @Query("SELECT s FROM Sale s WHERE s.part.id = :partId")
    List<Sale> findByPartId(@Param("partId") Long partId);

    // Find sales within date range
    List<Sale> findBySaleDateBetween(LocalDateTime start, LocalDateTime end);

    // Find sales by part, store, and date range
    @Query("SELECT s FROM Sale s WHERE s.part.id = :partId AND s.store.id = :storeId AND s.saleDate BETWEEN :start AND :end")
    List<Sale> findByPartAndStoreAndSaleDateBetween(
            @Param("partId") Long partId,
            @Param("storeId") Long storeId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    // Get total quantity sold for a part at a store within date range
    @Query("SELECT SUM(s.quantitySold) FROM Sale s WHERE s.part.id = :partId AND s.store.id = :storeId AND s.saleDate BETWEEN :start AND :end")
    Integer getTotalQuantitySold(
            @Param("partId") Long partId,
            @Param("storeId") Long storeId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    // Get top selling parts across all stores
    @Query("SELECT s.part.id, s.part.name, SUM(s.quantitySold) as total " +
            "FROM Sale s " +
            "GROUP BY s.part.id, s.part.name " +
            "ORDER BY total DESC")
    List<Object[]> getTopSellingParts();
}