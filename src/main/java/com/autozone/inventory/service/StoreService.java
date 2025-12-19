package com.autozone.inventory.service;

import com.autozone.inventory.entity.Store;
import com.autozone.inventory.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class StoreService {

    private final StoreRepository storeRepository;

    public List<Store> getAllStores(){
        return storeRepository.findAll();
    }

    public Optional<Store> getStoreById(Long id){
        return storeRepository.findById(id);
    }

    public Optional<Store> getStoreByStoreNumber(String storeNumber){
        return storeRepository.findByStoreNumber(storeNumber);
    }

    public Store createStore(Store store){
        if (storeRepository.existsByStoreNumber(store.getStoreNumber())){
            throw new IllegalArgumentException("Store with number " + store.getStoreNumber() + " already exists");
        }
        return storeRepository.save(store);
    }

    public Store updateStore(Long id, Store storeDetails){
        return storeRepository.findById(id)
                .map(store -> {
                    store.setName(storeDetails.getName());
                    store.setAddress(storeDetails.getAddress());
                    store.setCity(storeDetails.getCity());
                    store.setState(storeDetails.getState());
                    store.setZipCode(storeDetails.getZipCode());
                    store.setPhone(storeDetails.getPhone());
                    store.setStoreType(storeDetails.getStoreType());
                    store.setStoreType(storeDetails.getStoreType());
                    return storeRepository.save(store);
                })
                .orElseThrow(() -> new RuntimeException("Store not found with id: " + id));
    }
    public void deleteStore(Long id) {
        storeRepository.findById(id).ifPresent(store -> {
            store.setDeleted(true);
            storeRepository.save(store);
        });
    }
}
