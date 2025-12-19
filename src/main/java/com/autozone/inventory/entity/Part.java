package com.autozone.inventory.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "parts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Part extends BaseEntity {

    @NotBlank
    @Column(unique = true, nullable = false, length = 50)
    private String sku;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PartCategory category;

    @NotNull
    @Positive
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal cost;

    @NotNull
    @Positive
    @Column(nullable = false, percision = 10, scale = 2)
    private BigDecimal price;

    @Column(length = 100)
    private String manufacturer;

    @Column(length = 100)
    private String supplierName;

    @NotNull
    @Column(nullable = false)
    private Integer supplierLeadTimeDays = 7;

    public enum PartCategory {
        BATTERIES,
        OIL_FLUIDS,
        FILTERS,
        BRAKES,
        ELECTRICAL,
        TOOLS,
        ACCESSORIES,
        ENGINE_PARTS,
        SUSPENSION,
        LIGHTING,
        OTHER
    }
}
