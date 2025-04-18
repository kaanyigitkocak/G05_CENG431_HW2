package com.manufacturing.system.data;

import com.manufacturing.system.domain.model.*;
import com.manufacturing.system.presentation.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProductRepository {
    private final List<Product> products;
    private final List<ProductionOrder> productionOrders;
    private final ComponentRepository componentRepository;
    private final Logger logger;
    private final String productsCsvPath;

    public ProductRepository(String productsCsvPath, ComponentRepository componentRepository, Logger logger) {
        this.products = new ArrayList<>();
        this.productionOrders = new ArrayList<>();
        this.componentRepository = componentRepository;
        this.logger = logger;
        this.productsCsvPath = productsCsvPath;
    }

    public void loadProducts() {
        List<Map<String, String>> records = FileParser.parseCSV(productsCsvPath);
        if (records.isEmpty()) {
            logger.log("Warning: Product CSV file is empty or could not be read. File path: " + productsCsvPath);
            return;
        }

        logger.log("Products are loading...");
        int successCount = 0;
        int errorCount = 0;

        for (Map<String, String> record : records) {
            try {
                String name = record.get("Product Name");
                String quantityStr = record.get("Quantity");

                if (name == null || name.isEmpty()) {
                    logger.log("Error: Product name is missing - " + record);
                    errorCount++;
                    continue;
                }

                String id = name.replaceAll("\\s+", "_").toLowerCase();
                int quantity = 0;
                try {
                    if (quantityStr != null && !quantityStr.isEmpty()) {
                        quantity = Integer.parseInt(quantityStr.trim());
                    } else {
                        logger.log("Warning: Quantity value is missing, 0 will be used - Product: " + name);
                    }
                } catch (NumberFormatException e) {
                    logger.log("Error: Invalid quantity value '" + quantityStr + "' - Product: " + name + ". Error: " + e.getMessage());
                    errorCount++;
                    continue;
                }

                Product product = new Product(id, name, 0);
                logger.log("The product is created: " + name + " (ID: " + id + ")");

                // Check all components in CSV and add to product
                List<Component> allComponents = componentRepository.getAll();
                for (Component component : allComponents) {
                    String componentQuantityStr = record.get(component.getName());
                    if (componentQuantityStr != null && !componentQuantityStr.isEmpty() && !componentQuantityStr.equals("0")) {
                        try {
                            // Double parse using dot instead of comma
                            double componentQuantityDouble = Double.parseDouble(componentQuantityStr.replace(',', '.'));
                            int componentQuantity = (int)Math.ceil(componentQuantityDouble);
                            
                            if (componentQuantity > 0) {
                                product.addComponent(component, componentQuantity);
                                logger.log("Component Added: " + component.getName() + " (ID: " + component.getId() + ", Quantity: " + componentQuantity + ")");
                            }
                        } catch (NumberFormatException e) {
                            logger.log("Error: Invalid component quantity value '" + componentQuantityStr + "' - Component: " + component.getName());
                        }
                    }
                }

                logger.log("Product details: " + name + " - Total Cost: " + product.getCost() + " TL, Total Weight: " + product.getWeight() + " kg");

                products.add(product);
                successCount++;
            } catch (Exception e) {
                logger.log("Unexpected Error: " + e.getMessage() + " could not resolve the record: " + record);
                errorCount++;
            }
        }

        logger.log("Product installation completed. Success: " + successCount + ", Error: " + errorCount);
        logger.log("Total number of products: " + products.size());
    }

    public List<Product> getAll() {
        return products;
    }

    public Product findById(String id) {
        return products.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
    
    // Product finding method by product name
    public Product findByName(String name) {
        return products.stream()
                .filter(p -> p.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public void addProductionOrder(ProductionOrder order) {
        productionOrders.add(order);
    }

    public List<ProductionOrder> getProductionOrders() {
        return productionOrders;
    }
} 