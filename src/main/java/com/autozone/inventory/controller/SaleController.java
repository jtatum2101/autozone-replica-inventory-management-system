package com.autozone.inventory.controller;

import com.autozone.inventory.entity.Sale;
import com.autozone.inventory.repository.SaleRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sales")
@RequiredArgsConstructor
@Tag(name = "Sales", description = "Sales Management API")
public class SaleController {

    private final SaleRepository saleRepository;

    @GetMapping
    @Operation(summary = "Get all sales")
    public ResponseEntity<List<Sale>> getAllSales() {
        return ResponseEntity.ok(saleRepository.findAll());
    }

    @GetMapping("/store/{storeId}")
    @Operation(summary = "Get sales by store")
    public ResponseEntity<List<Sale>> getSalesByStore(@PathVariable Long storeId) {
        return ResponseEntity.ok(saleRepository.findByStoreId(storeId));
    }

    @GetMapping("/part/{partId}")
    @Operation(summary = "Get sales by part")
    public ResponseEntity<List<Sale>> getSalesByPart(@PathVariable Long partId) {
        return ResponseEntity.ok(saleRepository.findByPartId(partId));
    }

    @GetMapping("/top-selling")
    @Operation(summary = "Get top selling parts")
    public ResponseEntity<List<Map<String, Object>>> getTopSellingParts(
            @RequestParam(defaultValue = "10") int limit) {
        List<Object[]> topParts = saleRepository.getTopSellingParts();

        List<Map<String, Object>> result = topParts.stream()
                .limit(limit)
                .map(row -> Map.of(
                        "partId", row[0],
                        "partName", row[1],
                        "totalQuantity", row[2]
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }
}