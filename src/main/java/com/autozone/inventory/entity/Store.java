package com.autozone.inventory.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Entity
@Table(name = "stores")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Store extends BaseEntity{

    @NotBlank
    @Column(unique = true, nullable = false, length = 10)
    private String storeNumber;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Column(nullable = false)
    private String address;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String city;

    @NotBlank
    @Column(nullable = false, length = 2)
    private String state;

    @NotBlank
    @Pattern(regexp = "^\\d{5}(-\\d{4})?$")
    private String zipCode;

    @Column(length = 20)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StoreType storeType = StoreType.STANDARD;

    public enum StoreType {
        HUB,
        STANDARD,
        COMMERCIAL
    }
}
