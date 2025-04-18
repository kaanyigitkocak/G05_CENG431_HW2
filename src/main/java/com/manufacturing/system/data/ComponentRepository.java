package com.manufacturing.system.data;

import com.manufacturing.system.domain.model.*;
import com.manufacturing.system.presentation.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ComponentRepository {
    private final List<Component> components;
    private final Logger logger;
    private final String componentsCsvPath;

    public ComponentRepository(String componentsCsvPath, Logger logger) {
        this.components = new ArrayList<>();
        this.logger = logger;
        this.componentsCsvPath = componentsCsvPath;
    }

    public void loadComponents() {
        List<Map<String, String>> records = FileParser.parseCSV(componentsCsvPath);
        if (records.isEmpty()) {
            logger.log("Warning: Component CSV file is empty or unreadable. File path " + componentsCsvPath);
            return;
        }

        logger.log("Loading components...");
        int successCount = 0;
        int errorCount = 0;

        for (Map<String, String> record : records) {
            try {
                // Get required fields from CSV file
                String name = record.get("Component");
                String costStr = record.get("Unit Cost (TL)");
                String weightStr = record.get("Unit Weight (kg)");
                String type = record.get("Type");
                String stockStr = record.get("Stock Quantity");

                if (name == null || name.isEmpty()) {
                    logger.log("Error: Component name missing");
                    errorCount++;
                    continue;
                }
                if (type == null || type.isEmpty()) {
                    logger.log("Error: Component type is missing - Component: " + name);
                    errorCount++;
                    continue;
                }

                String id = name.replaceAll("\\s+", "_").toLowerCase();

                double cost = 0.0;
                try {
                    if (costStr != null && !costStr.isEmpty()) {
                        cost = Double.parseDouble(costStr);
                    } else {
                        logger.log("Warning: Cost value is missing, 0 will be used - Component: " + name);
                    }
                } catch (NumberFormatException e) {
                    logger.log("Error: Invalid cost value '" + costStr + "' - Component: " + name + ". Error: " + e.getMessage());
                    errorCount++;
                    continue;
                }

                float weight = 0.0f;
                try {
                    if (weightStr != null && !weightStr.isEmpty()) {
                        weight = Float.parseFloat(weightStr);
                    } else {
                        logger.log("Warning: Weight value is missing, 0 will be used - Component: " + name);
                    }
                } catch (NumberFormatException e) {
                    logger.log("Error: Invalid weight value '" + weightStr + "' - Component: " + name + ". Error: " + e.getMessage());
                    errorCount++;
                    continue;
                }

                int stock = 0;
                try {
                    if (stockStr != null && !stockStr.isEmpty()) {
                        String stockNumeric = stockStr.split("\\s+")[0];
                        stock = Integer.parseInt(stockNumeric);
                    } else {
                        logger.log("Warning: Stock value is missing, 0 will be used - Component: " + name);
                    }
                } catch (Exception e) {
                    logger.log("Error: Invalid stock value '" + stockStr + "' - Component: " + name + ". Error: " + e.getMessage());
                    errorCount++;
                    continue;
                }

                Component component;
                switch (type.toLowerCase().trim()) {
                    case "rawmaterial":
                    case "raw material":
                        component = new RawMaterial(id, name, cost, weight, stock);
                        logger.log("Raw material added: " + name + " (ID: " + id + ", Cost: " + cost + ", Weight: " + weight + ", Stock: " + stock + ")");
                        break;
                    case "paint":
                        component = new Paint(id, name, cost, weight, stock);
                        logger.log("Paint added: " + name + " (ID: " + id + ", Cost: " + cost + ", Weight: " + weight + ", Stock: " + stock + ")");
                        break;
                    case "hardware":
                        component = new Hardware(id, name, cost, weight, stock);
                        logger.log("Hardware added: " + name + " (ID: " + id + ", Cost: " + cost + ", Weight: " + weight + ", Stock: " + stock + ")");
                        break;
                    default:
                        logger.log("Error: Unknown component type: " + type + " - Component: " + name);
                        errorCount++;
                        continue;
                }

                components.add(component);
                successCount++;
            } catch (Exception e) {
                logger.log("Unexpected error: " + e.getMessage() + " Record processing failed: " + record);
                errorCount++;
            }
        }

        logger.log("Component installation completed. Success: " + successCount + ", Incorrect: " + errorCount);
        logger.log("Total number of components: " + components.size());
    }

    public List<Component> getAll() {
        return components;
    }

    public Component findById(String id) {
        return components.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Component findByName(String name) {
        return components.stream()
                .filter(c -> c.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
}