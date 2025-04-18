package com.manufacturing.system.domain.model;

public class ComponentQuantity {
    private final Component component;
    private final int quantity;
    
    public ComponentQuantity(Component component, int quantity) {
        this.component = component;
        this.quantity = quantity;
    }
    
    public Component getComponent() {
        return component;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public double getTotalCost() {
        return component.getCost() * quantity;
    }
    
    public double getTotalWeight() {
        return component.getWeight() * quantity;
    }
    
    @Override
    public String toString() {
        return String.format("%dx %s", quantity, component.getName());
    }
} 