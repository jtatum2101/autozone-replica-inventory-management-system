package com.autozone.inventory.dataloader;

import com.autozone.inventory.entity.*;
import com.autozone.inventory.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final StoreRepository storeRepository;
    private final PartRepository partRepository;
    private final InventoryRepository inventoryRepository;
    private final SaleRepository saleRepository;

    private final Random random = new Random();

    @Override
    public void run(String... args) {
        // Only load data if database is empty
        if (storeRepository.count() > 0) {
            log.info("Database already contains data. Skipping seed data.");
            return;
        }

        log.info("Loading seed data...");

        // Create stores
        List<Store> stores = createStores();
        log.info("Created {} stores", stores.size());

        // Create parts
        List<Part> parts = createParts();
        log.info("Created {} parts", parts.size());

        // Create inventory
        List<Inventory> inventoryItems = createInventory(stores, parts);
        log.info("Created {} inventory items", inventoryItems.size());

        // Create sales history
        int salesCount = createSalesHistory(stores, parts);
        log.info("Created {} sales records", salesCount);

        log.info("Seed data loading completed!");
    }

    private List<Store> createStores() {
        List<Store> stores = new ArrayList<>();

        stores.add(Store.builder()
                .storeNumber("5421")
                .name("AutoZone - Southaven")
                .address("7855 Southcrest Pkwy")
                .city("Southaven")
                .state("MS")
                .zipCode("38671")
                .phone("(662) 349-8600")
                .storeType(Store.StoreType.STANDARD)
                .build());

        stores.add(Store.builder()
                .storeNumber("5001")
                .name("AutoZone - Memphis Hub")
                .address("3050 Austin Peay Hwy")
                .city("Memphis")
                .state("TN")
                .zipCode("38128")
                .phone("(901) 386-2100")
                .storeType(Store.StoreType.HUB)
                .build());

        stores.add(Store.builder()
                .storeNumber("5422")
                .name("AutoZone - Germantown")
                .address("2145 W Poplar Ave")
                .city("Germantown")
                .state("TN")
                .zipCode("38138")
                .phone("(901) 754-8900")
                .storeType(Store.StoreType.STANDARD)
                .build());

        stores.add(Store.builder()
                .storeNumber("5733")
                .name("AutoZone - Commercial Memphis")
                .address("4880 Summer Ave")
                .city("Memphis")
                .state("TN")
                .zipCode("38122")
                .phone("(901) 682-3400")
                .storeType(Store.StoreType.COMMERCIAL)
                .build());

        stores.add(Store.builder()
                .storeNumber("5824")
                .name("AutoZone - Olive Branch")
                .address("6738 Goodman Rd")
                .city("Olive Branch")
                .state("MS")
                .zipCode("38654")
                .phone("(662) 890-1200")
                .storeType(Store.StoreType.STANDARD)
                .build());

        return storeRepository.saveAll(stores);
    }

    private List<Part> createParts() {
        List<Part> parts = new ArrayList<>();

        // Batteries
        parts.add(Part.builder()
                .sku("DU-48AGM")
                .name("Duralast Platinum AGM Battery Group 48")
                .description("800 CCA, 3-year warranty, AGM technology")
                .category(Part.PartCategory.BATTERIES)
                .cost(new BigDecimal("89.99"))
                .price(new BigDecimal("189.99"))
                .manufacturer("Duralast")
                .supplierName("Clarios")
                .supplierLeadTimeDays(3)
                .build());

        parts.add(Part.builder()
                .sku("VL-65-850")
                .name("Valucraft Battery Group 65")
                .description("850 CCA, 2-year warranty")
                .category(Part.PartCategory.BATTERIES)
                .cost(new BigDecimal("59.99"))
                .price(new BigDecimal("129.99"))
                .manufacturer("Valucraft")
                .supplierName("Clarios")
                .supplierLeadTimeDays(3)
                .build());

        // Oil & Fluids
        parts.add(Part.builder()
                .sku("MOBIL1-5W30-5QT")
                .name("Mobil 1 Full Synthetic 5W-30 Motor Oil - 5 Quart")
                .description("Advanced full synthetic motor oil")
                .category(Part.PartCategory.OIL_FLUIDS)
                .cost(new BigDecimal("18.99"))
                .price(new BigDecimal("34.99"))
                .manufacturer("Mobil")
                .supplierName("ExxonMobil")
                .supplierLeadTimeDays(5)
                .build());

        parts.add(Part.builder()
                .sku("VAL-5W20-5QT")
                .name("Valucraft Conventional 5W-20 Motor Oil - 5 Quart")
                .description("Quality conventional motor oil")
                .category(Part.PartCategory.OIL_FLUIDS)
                .cost(new BigDecimal("9.99"))
                .price(new BigDecimal("19.99"))
                .manufacturer("Valucraft")
                .supplierName("Phillips 66")
                .supplierLeadTimeDays(7)
                .build());

        parts.add(Part.builder()
                .sku("PRESTONE-AF2100")
                .name("Prestone 50/50 Prediluted Antifreeze - 1 Gallon")
                .description("Ready to use antifreeze coolant")
                .category(Part.PartCategory.OIL_FLUIDS)
                .cost(new BigDecimal("7.99"))
                .price(new BigDecimal("14.99"))
                .manufacturer("Prestone")
                .supplierName("Prestone Products")
                .supplierLeadTimeDays(5)
                .build());

        // Filters
        parts.add(Part.builder()
                .sku("STP-S10575")
                .name("STP Extended Life Oil Filter")
                .description("10,000 mile protection")
                .category(Part.PartCategory.FILTERS)
                .cost(new BigDecimal("3.99"))
                .price(new BigDecimal("8.99"))
                .manufacturer("STP")
                .supplierName("Spectrum Brands")
                .supplierLeadTimeDays(7)
                .build());

        parts.add(Part.builder()
                .sku("BOSCH-5430")
                .name("Bosch Workshop Engine Air Filter")
                .description("OE quality air filtration")
                .category(Part.PartCategory.FILTERS)
                .cost(new BigDecimal("8.99"))
                .price(new BigDecimal("16.99"))
                .manufacturer("Bosch")
                .supplierName("Bosch Auto Parts")
                .supplierLeadTimeDays(5)
                .build());

        parts.add(Part.builder()
                .sku("WIX-24977")
                .name("WIX Cabin Air Filter")
                .description("99% pollen filtration")
                .category(Part.PartCategory.FILTERS)
                .cost(new BigDecimal("9.99"))
                .price(new BigDecimal("19.99"))
                .manufacturer("WIX")
                .supplierName("WIX Filters")
                .supplierLeadTimeDays(5)
                .build());

        // Brakes
        parts.add(Part.builder()
                .sku("DL-MKD1089")
                .name("Duralast Gold Brake Pad Set")
                .description("Ceramic brake pads with hardware")
                .category(Part.PartCategory.BRAKES)
                .cost(new BigDecimal("29.99"))
                .price(new BigDecimal("64.99"))
                .manufacturer("Duralast")
                .supplierName("Akebono")
                .supplierLeadTimeDays(5)
                .build());

        parts.add(Part.builder()
                .sku("DL-BR900842")
                .name("Duralast Brake Rotor")
                .description("Premium quality brake rotor")
                .category(Part.PartCategory.BRAKES)
                .cost(new BigDecimal("24.99"))
                .price(new BigDecimal("54.99"))
                .manufacturer("Duralast")
                .supplierName("Akebono")
                .supplierLeadTimeDays(5)
                .build());

        // Electrical
        parts.add(Part.builder()
                .sku("DL-DLG100")
                .name("Duralast Gold Alternator")
                .description("140 amp, remanufactured, lifetime warranty")
                .category(Part.PartCategory.ELECTRICAL)
                .cost(new BigDecimal("89.99"))
                .price(new BigDecimal("199.99"))
                .manufacturer("Duralast")
                .supplierName("Remy")
                .supplierLeadTimeDays(3)
                .build());

        parts.add(Part.builder()
                .sku("DL-17854")
                .name("Duralast Starter")
                .description("Remanufactured starter motor")
                .category(Part.PartCategory.ELECTRICAL)
                .cost(new BigDecimal("59.99"))
                .price(new BigDecimal("129.99"))
                .manufacturer("Duralast")
                .supplierName("Remy")
                .supplierLeadTimeDays(3)
                .build());

        // Wiper Blades
        parts.add(Part.builder()
                .sku("RAIN-X-22")
                .name("Rain-X Latitude Water Repellency 22in Wiper Blade")
                .description("Premium beam wiper blade")
                .category(Part.PartCategory.ACCESSORIES)
                .cost(new BigDecimal("11.99"))
                .price(new BigDecimal("24.99"))
                .manufacturer("Rain-X")
                .supplierName("ITW Global Brands")
                .supplierLeadTimeDays(7)
                .build());

        // Spark Plugs
        parts.add(Part.builder()
                .sku("NGK-3403")
                .name("NGK G-Power Platinum Spark Plug")
                .description("Platinum spark plug for performance")
                .category(Part.PartCategory.ENGINE_PARTS)
                .cost(new BigDecimal("3.49"))
                .price(new BigDecimal("7.99"))
                .manufacturer("NGK")
                .supplierName("NGK Spark Plugs")
                .supplierLeadTimeDays(5)
                .build());

        // Lighting
        parts.add(Part.builder()
                .sku("SYLVANIA-H11")
                .name("Sylvania H11 SilverStar Ultra Headlight Bulb")
                .description("High performance halogen bulb")
                .category(Part.PartCategory.LIGHTING)
                .cost(new BigDecimal("19.99"))
                .price(new BigDecimal("39.99"))
                .manufacturer("Sylvania")
                .supplierName("OSRAM")
                .supplierLeadTimeDays(7)
                .build());

        return partRepository.saveAll(parts);
    }

    private List<Inventory> createInventory(List<Store> stores, List<Part> parts) {
        List<Inventory> inventoryItems = new ArrayList<>();

        for (Store store : stores) {
            for (Part part : parts) {
                // Different inventory levels based on store type and part category
                int baseQuantity = getBaseQuantity(store.getStoreType(), part.getCategory());
                int quantity = baseQuantity + random.nextInt(20);

                int reorderPoint = calculateReorderPoint(part.getCategory());
                int reorderQuantity = calculateReorderQuantity(part.getCategory());
                int maxStock = reorderQuantity * 4;

                inventoryItems.add(Inventory.builder()
                        .part(part)
                        .store(store)
                        .quantity(quantity)
                        .reorderPoint(reorderPoint)
                        .reorderQuantity(reorderQuantity)
                        .maxStockLevel(maxStock)
                        .location(generateLocation())
                        .build());
            }
        }

        return inventoryRepository.saveAll(inventoryItems);
    }

    private int createSalesHistory(List<Store> stores, List<Part> parts) {
        List<Sale> sales = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        // Generate 90 days of sales history
        for (int daysAgo = 90; daysAgo >= 0; daysAgo--) {
            LocalDateTime saleDate = now.minusDays(daysAgo);

            for (Store store : stores) {
                // Each store sells 2-5 different parts per day
                int salesPerDay = 2 + random.nextInt(4);

                for (int i = 0; i < salesPerDay; i++) {
                    Part part = parts.get(random.nextInt(parts.size()));
                    int quantity = getSaleQuantity(part.getCategory());

                    BigDecimal unitPrice = part.getPrice();
                    BigDecimal totalPrice = unitPrice.multiply(new BigDecimal(quantity));

                    sales.add(Sale.builder()
                            .part(part)
                            .store(store)
                            .quantitySold(quantity)
                            .unitPrice(unitPrice)
                            .totalPrice(totalPrice)
                            .saleDate(saleDate.plusHours(random.nextInt(12) + 8)) // Sales between 8am-8pm
                            .soldBy("Employee-" + (100 + random.nextInt(50)))
                            .build());
                }
            }
        }

        saleRepository.saveAll(sales);
        return sales.size();
    }

    private int getBaseQuantity(Store.StoreType storeType, Part.PartCategory category) {
        int base = switch (storeType) {
            case HUB -> 100;
            case COMMERCIAL -> 50;
            case STANDARD -> 30;
        };

        // Fast-moving categories get more stock
        return switch (category) {
            case BATTERIES, OIL_FLUIDS, FILTERS -> base;
            case BRAKES -> (int) (base * 0.8);
            case ELECTRICAL, ENGINE_PARTS -> (int) (base * 0.6);
            default -> (int) (base * 0.5);
        };
    }

    private int calculateReorderPoint(Part.PartCategory category) {
        return switch (category) {
            case BATTERIES, OIL_FLUIDS -> 15;
            case FILTERS, BRAKES -> 20;
            case ELECTRICAL -> 8;
            case ENGINE_PARTS -> 25;
            default -> 10;
        };
    }

    private int calculateReorderQuantity(Part.PartCategory category) {
        return switch (category) {
            case BATTERIES -> 30;
            case OIL_FLUIDS -> 50;
            case FILTERS -> 40;
            case BRAKES -> 30;
            case ELECTRICAL -> 15;
            case ENGINE_PARTS -> 50;
            default -> 25;
        };
    }

    private int getSaleQuantity(Part.PartCategory category) {
        return switch (category) {
            case OIL_FLUIDS, FILTERS -> 1 + random.nextInt(3); // 1-3 units
            case BATTERIES, BRAKES -> 1; // Usually 1 at a time
            case ENGINE_PARTS -> 1 + random.nextInt(5); // Spark plugs sold in sets
            default -> 1 + random.nextInt(2);
        };
    }

    private String generateLocation() {
        char aisle = (char) ('A' + random.nextInt(8)); // A-H
        int shelf = 1 + random.nextInt(5); // 1-5
        int bin = 1 + random.nextInt(10); // 1-10
        return String.format("%c-%d-%d", aisle, shelf, bin);
    }
}