package com.autozone.inventory.repository;

import com.autozone.inventory.entity.Part;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PartRepository extends JpaRepository<Part, Long>{
    Optional<Part> findBySku(String sku);

    Boolean existsBySku(String sku);

    List<Part> findByCategory(Part.PartCategory category);

    @Query("SELECT p FROM Part p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Part> searchByName(String searchTerm);
}
