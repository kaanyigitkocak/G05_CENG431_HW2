package com.manufacturing.system.domain.model;

import java.util.ArrayList;
import java.util.List;

public class Product implements Component {
    private final String id;
    private final String name;
    private int stock;
    private final List<ComponentQuantity> components = new ArrayList<>();
    
    public Product(String id, String name, int stock) {
        this.id = id;
        this.name = name;
        this.stock = stock;
    }
    
    @Override
    public void add(Component component) {
        add(component, 1);
    }
    
    public void add(Component component, int quantity) {
        components.add(new ComponentQuantity(component, quantity));
    }
    
    @Override
    public double getCost() {
        double totalCost = 0;
        for (ComponentQuantity cq : components) {
            totalCost += cq.getTotalCost();
        }
        return totalCost;
    }
    
    @Override
    public double getWeight() {
        double totalWeight = 0;
        for (ComponentQuantity cq : components) {
            totalWeight += cq.getTotalWeight();
        }
        return totalWeight;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    public String getId() {
        return id;
    }
    
    @Override
    public int getStock() {
        return stock;
    }
    
    public List<ComponentQuantity> getComponents() {
        return new ArrayList<>(components);
    }
    
    @Override
    public boolean useStock(int quantity) {
        if (stock >= quantity) {
            stock -= quantity;
            return true;
        }
        return false;
    }
    
    /**
     * Checks the stock of all components contained in the product
     * @return If stock is sufficient for all components, returns true; otherwise false
     */
    public boolean checkComponentsStock() {
        for (ComponentQuantity cq : components) {
            Component c = cq.getComponent();
            if (c.getStock() < cq.getQuantity()) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Uses the specified amount of all ingredients of the product
     * @return if successful true, otherwise false
     */
    public boolean useComponentsStock() {
        if (!checkComponentsStock()) {
            return false;
        }
        
        for (ComponentQuantity cq : components) {
            Component c = cq.getComponent();
            if (!c.useStock(cq.getQuantity())) {
                return false; 
            }
        }
        stock++;
        return true;
    }
    
    @Override
    public String generateReport() {
        StringBuilder report = new StringBuilder();
        report.append(String.format("Product: %s (ID: %s) - Total Cost: %.2f, Total Weight: %.2f, Stock: %d\n", 
                                    name, id, getCost(), getWeight(), stock));
        report.append("Components:\n");
        
        for (ComponentQuantity cq : components) {
            report.append("  - ").append(cq.toString()).append("\n");
        }
        
        return report.toString();
    }
    
    @Override
    public String toString() {
        return name;
    }
    
    /**
     * Adds a specified amount of an ingredient to a product.
     * 
     * @param component Component to add
     * @param quantity Its amount
     */
    public void addComponent(Component component, int quantity) {
        add(component, quantity);
    }
} 