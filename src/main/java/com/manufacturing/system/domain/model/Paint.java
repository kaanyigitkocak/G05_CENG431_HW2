package com.manufacturing.system.domain.model;

/**
 * Boya bileşeni (Leaf)
 */
public class Paint extends BaseComponent {
    
    public Paint(String id, String name, double cost, double weight, int stock) {
        super(id, name, cost, weight, stock);
    }
    
    @Override
    public String generateReport() {
        return "Boya: " + super.generateReport();
    }
} 