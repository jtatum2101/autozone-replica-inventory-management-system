package com.autozone.inventory.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@Entity
@Table(name = "inventory", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"part_id", "store_id"})

})

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inventory extends BaseEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "part_id", nullable = false)
    private Part part;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @NotNull
    @PositiveOrZero
    @Column(nullable = false)
    private Integer quantity = 0;

    @NotNull
    @PositiveOrZero
    @Column(nullable = false)
    private Integer reorderPoint = 10;

    @NotNull
    @PositiveOrZero
    @Column(nullable = false)
    private Integer reorderQuantity = 50;

    @NotNull
    @PositiveOrZero
    @Column(nullable = false)
    private Integer maxStockLevel = 200;

    @Column(length = 50)
    private String location; //Aisle/Bin location while in store



}
