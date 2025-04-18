package com.manufacturing.system.domain.model;

public class ProductionOrder {
    private final Product product;
    private final int quantity;
    private boolean completed;
    
    public ProductionOrder(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        this.completed = false;
    }

    public Product getProduct() {
        return product;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public boolean isCompleted() {
        return completed;
    }
    
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public double getTotalCost() {
        return product.getCost() * quantity;
    }

    public double getTotalWeight() {
        return product.getWeight() * quantity;
    }
    
    @Override
    public String toString() {
        return String.format("Production Order: %s - %d quantity, Unit Cost: %.2f TL, Unit Weight: %.2f kg, " +
                            "Total Cost: %.2f TL, Total Weight: %.2f kg, Situation: %s",
                            product.getName(), quantity, product.getCost(), product.getWeight(),
                            getTotalCost(), getTotalWeight(), completed ? "Completed" : "Waiting");
    }
} 