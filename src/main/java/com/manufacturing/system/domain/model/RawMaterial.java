package com.manufacturing.system.domain.model;

public class RawMaterial extends BaseComponent {
    
    public RawMaterial(String id, String name, double cost, double weight, int stock) {
        super(id, name, cost, weight, stock);
    }
    
    @Override
    public String generateReport() {
        return "Raw Materials: " + super.generateReport();
    }
} 