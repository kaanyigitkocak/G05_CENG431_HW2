package com.manufacturing.system.domain.model;

/**
 * Component arayüzünü uygulayan temel soyut sınıf.
 * Tüm leaf (yaprak) bileşenler için ortak özellikleri barındırır.
 */
public abstract class BaseComponent implements Component {
    private final String id;
    private final String name;
    private final double cost;
    private final double weight;
    private int stock;
    
    public BaseComponent(String id, String name, double cost, double weight, int stock) {
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.weight = weight;
        this.stock = stock;
    }
    
    @Override
    public double getCost() {
        return cost;
    }
    
    @Override
    public double getWeight() {
        return weight;
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
    
    @Override
    public boolean useStock(int quantity) {
        if (stock >= quantity) {
            stock -= quantity;
            return true;
        }
        return false;
    }
    
    @Override
    public String generateReport() {
        return String.format("%s (ID: %s) - Maliyet: %.2f, Ağırlık: %.2f, Stok: %d", 
                            name, id, cost, weight, stock);
    }
    
    @Override
    public String toString() {
        return generateReport();
    }
} 