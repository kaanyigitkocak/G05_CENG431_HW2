package com.manufacturing.system.domain.state;

import com.manufacturing.system.infrastructure.RandomGenerator;

public class WaitingForStockState implements ManufacturingState {
    
    @Override
    public void handle(ManufacturingProcess context) {
        System.out.println("Stock control is being carried out...");
        
        // Check if we should simulate stock shortage
        boolean simulateStockShortage = RandomGenerator.shouldSimulateStockShortage();
        
        if (context.getProduct().checkComponentsStock() && !simulateStockShortage) {
            System.out.println("The stock is sufficient. The production phase is starting.");
            context.setState(new InManufacturingState());
            context.getCurrentState().handle(context);
        } else {
            String reason = simulateStockShortage ? 
                "Logistic delays caused stock shortage" :
                "Insufficient stock in warehouse";
                
            System.out.println("Insufficient stock: " + reason + ". Production failure.");
            context.setFailureReason("Insufficient Stock");
            context.setState(new FailedState("Insufficient Stock"));
            context.incrementStockShortageCount();
            context.getCurrentState().handle(context);
        }
    }
    
    @Override
    public String getName() {
        return "Stock Control";
    }
} 