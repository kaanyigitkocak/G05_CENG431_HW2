package com.manufacturing.system.domain.model;

public class Hardware extends BaseComponent {
    
    public Hardware(String id, String name, double cost, double weight, int stock) {
        super(id, name, cost, weight, stock);
    }
    
    @Override
    public String generateReport() {
        return "Hardware: " + super.generateReport();
    }
} 