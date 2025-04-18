package com.manufacturing.system.domain.state;

public class CompletedState implements ManufacturingState {
    
    @Override
    public void handle(ManufacturingProcess context) {
        System.out.println("Production completed successfully.");
    }
    
    @Override
    public String getName() {
        return "Completed";
    }
} 