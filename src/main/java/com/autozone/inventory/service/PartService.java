package com.autozone.inventory.service;

import com.autozone.inventory.entity.Part;
import com.autozone.inventory.repository.PartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PartService {

    private final PartRepository partRepository;

    public List<Part> getAllParts(){
        return partRepository.findAll();
    }

    public Optional<Part> getPartById(Long id){
        return partRepository.findById(id);
    }

    public Optional<Part> getPartBySku(String sku){
        return partRepository.findBySku(sku);
    }

    public List<Part> getPartsByCategory(Part.PartCategory category){
        return partRepository.findByCategory(category);
    }

    public List<Part> searchPartsByName(String searchTerm){
        return partRepository.searchByName(searchTerm);
    }

    public Part createPart(Part part){
        if (partRepository.existsBySku(part.getSku())){
            throw new IllegalArgumentException(("Part with SKU " + part.getSku() + " already exists"));
        }
        return partRepository.save(part);
    }

    public Part updatePart(Long id, Part partDetails){
        return partRepository.findById(id).map(part -> {
            part.setName(partDetails.getName());
            part.setDescription(partDetails.getDescription());
            part.setCategory(partDetails.getCategory());
            part.setCost(partDetails.getCost());
            part.setPrice(partDetails.getPrice());
            part.setManufacturer(partDetails.getManufacturer());
            part.setSupplierName(partDetails.getSupplierName());
            part.setSupplierLeadTimeDays(partDetails.getSupplierLeadTimeDays());
            return partRepository.save(part);
        })
                .orElseThrow(() -> new RuntimeException("Part not found with id: " + id));
    }
    public void deletePart(Long id) {
        partRepository.findById(id).ifPresent(part -> {
            part.setDeleted(true);
            partRepository.save(part);
        });
    }
}