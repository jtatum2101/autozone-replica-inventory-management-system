package com.autozone.inventory.controller;

import com.autozone.inventory.entity.Part;
import com.autozone.inventory.service.PartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/parts")
@RequiredArgsConstructor
@Tag(name = "Parts", description = "Auto Parts Management API")

public class PartController {
    private final PartService partService;
    @GetMapping
    @Operation(summary = "Get all parts")
    public ResponseEntity<List<Part>> getAllParts(){
        return ResponseEntity.ok(partService.getAllParts());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get part by ID")
    public ResponseEntity<Part> getPartById(@PathVariable Long id){
        return partService.getPartById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/sku/{sku}")
    @Operation(summary = "Get part by SKU")
    public ResponseEntity<Part> getPartBySku(@PathVariable String sku){
        return partService.getPartBySku(sku)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "Get parts by category")
    public ResponseEntity<List<Part>> getPartByCategory(@PathVariable Part.PartCategory category){
        return ResponseEntity.ok(partService.getPartsByCategory(category));
    }

    @GetMapping("/search")
    @Operation(summary = "Search parts by name")
    public ResponseEntity <List<Part>> searchParts(@RequestParam String name){
        return ResponseEntity.ok(partService.searchPartsByName(name));
    }

    @PostMapping
    @Operation(summary = "Create a new part")
    public ResponseEntity<Part> createPart(@Valid @RequestBody Part part){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(partService.createPart(part));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a part")
    public ResponseEntity<Part> updatePart(
            @PathVariable Long id,
            @Valid @RequestBody Part part){
        return ResponseEntity.ok(partService.updatePart(id, part));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a part")
    public ResponseEntity<Void> deletePart(@PathVariable Long id){
        partService.deletePart(id);
        return ResponseEntity.noContent().build();
    }
}
