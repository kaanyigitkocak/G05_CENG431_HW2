package com.manufacturing.system.domain.model;

public interface Component {

    double getCost();
    double getWeight();
    String getName();
    String getId();
    int getStock();
    String generateReport();
    default void add(Component component) {
        throw new UnsupportedOperationException(getName() + " cannot have subcomponents.");
    }
    
    boolean useStock(int quantity);
} 