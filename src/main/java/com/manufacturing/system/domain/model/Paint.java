package com.manufacturing.system.domain.model;


public class Paint extends BaseComponent {
    
    public Paint(String id, String name, double cost, double weight, int stock) {
        super(id, name, cost, weight, stock);
    }
    
    @Override
    public String generateReport() {
        return "Paint: " + super.generateReport();
    }
} 