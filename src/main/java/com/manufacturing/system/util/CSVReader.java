package com.manufacturing.system.util;

import com.manufacturing.system.data.FileParser;
import com.manufacturing.system.domain.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVReader {
    
    /**
     * Read components from CSV file
     * @param filePath path for CSV file
     * @return Mapped file data
     */
    public static Map<String, Component> readComponents(String filePath) {
        Map<String, Component> componentMap = new HashMap<>();
        List<Map<String, String>> records = FileParser.parseCSV(filePath);
        
        for (Map<String, String> record : records) {
            try {
                String id = record.get("id");
                String type = record.get("type");
                String name = record.get("name");
                
                double cost = 0.0;
                try {
                    String costStr = record.get("cost");
                    if (costStr != null && !costStr.isEmpty()) {
                        cost = Double.parseDouble(costStr);
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Invalid weight value: " + record.get("cost"));
                    continue;
                }
                
                double weight = 0.0;
                try {
                    String weightStr = record.get("weight");
                    if (weightStr != null && !weightStr.isEmpty()) {
                        weight = Double.parseDouble(weightStr);
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Invalid weight value: " + record.get("weight"));
                    continue;
                }
                
                int stock = 0;
                try {
                    String stockStr = record.get("stock");
                    if (stockStr != null && !stockStr.isEmpty()) {
                        stock = Integer.parseInt(stockStr);
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Invalid stock value: " + record.get("stock"));
                    continue;
                }
                
                Component component;
                if (type != null) {
                    switch (type.toLowerCase()) {
                        case "raw_material":
                        case "rawmaterial":
                            component = new RawMaterial(id, name, cost, weight, stock);
                            break;
                        case "paint":
                            component = new Paint(id, name, cost, weight, stock);
                            break;
                        case "hardware":
                            component = new Hardware(id, name, cost, weight, stock);
                            break;
                        default:
                            System.out.println("Unknown component type: " + type);
                            continue;
                    }
                    
                    componentMap.put(id, component);
                }
            } catch (Exception e) {
                System.err.println("Error while rendering component: " + e.getMessage());
            }
        }
        
        return componentMap;
    }
    
    /**
     * Reads products from CSV file
     * @param filePath path for CSV file
     * @param componentMap mapped component data
     * @return List of products and production quantities
     */
    public static List<ProductionOrder> readProducts(String filePath, Map<String, Component> componentMap) {
        List<ProductionOrder> orders = new ArrayList<>();
        List<Map<String, String>> records = FileParser.parseCSV(filePath);
        
        for (Map<String, String> record : records) {
            try {
                String id = record.get("id");
                String name = record.get("name");
                
                int quantity = 1; // Default value
                try {
                    String quantityStr = record.get("quantity");
                    if (quantityStr != null && !quantityStr.isEmpty()) {
                        quantity = Integer.parseInt(quantityStr);
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Invalid quantity value " + record.get("quantity"));
                }
                
                Product product = new Product(id, name, 0);
                
                String componentIdsStr = record.get("component_ids");
                String componentQtysStr = record.get("component_quantities");
                
                if (componentIdsStr != null && componentQtysStr != null) {
                    String[] componentIds = componentIdsStr.split(",");
                    String[] componentQtys = componentQtysStr.split(",");
                    
                    for (int i = 0; i < componentIds.length && i < componentQtys.length; i++) {
                        String componentId = componentIds[i].trim();
                        int componentQty = Integer.parseInt(componentQtys[i].trim());
                        
                        Component component = componentMap.get(componentId);
                        if (component != null) {
                            product.add(component, componentQty);
                        } else {
                            System.out.println("Product " + id + " could not be found: " + componentId);
                        }
                    }
                }
                
                orders.add(new ProductionOrder(product, quantity));
            } catch (Exception e) {
                System.err.println("Error while rendering product: " + e.getMessage());
            }
        }
        
        return orders;
    }

    public static class ProductionOrder {
        private final Product product;
        private final int quantity;
        
        public ProductionOrder(Product product, int quantity) {
            this.product = product;
            this.quantity = quantity;
        }
        
        public Product getProduct() {
            return product;
        }
        
        public int getQuantity() {
            return quantity;
        }
    }
} 