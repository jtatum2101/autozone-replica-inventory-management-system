package com.autozone.inventory.repository;

import com.autozone.inventory.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface StoreRepository extends JpaRepository<Store, Long>{

    Optional<Store> findByStoreNumber(String storeNumber);
    Boolean existsByStoreNumber(String storeNumber);
}
