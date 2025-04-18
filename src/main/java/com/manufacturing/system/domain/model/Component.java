package com.manufacturing.system.domain.model;

public interface Component {

    double getCost();
    double getWeight();
    String getName();
    String getId();
    int getStock();
    String generateReport();
    // Adds another component (not supported by default)
    default void add(Component component) {
        throw new UnsupportedOperationException(getName() + " cannot have subcomponents.");
    }
    
    /**
     * Uses the specified amount of stock from the component
     * @param quantity Amount to use
     * @return if successful true, otherwise false
     */
    boolean useStock(int quantity);
} 