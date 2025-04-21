package com.manufacturing.system.domain.state;

import com.manufacturing.system.infrastructure.RandomGenerator;

public class InManufacturingState implements ManufacturingState {
    
    @Override
    public void handle(ManufacturingProcess context) {
        System.out.println("During production...");

        // First check if components are available without consuming them
        if (!context.getProduct().checkComponentsStock()) {
            System.out.println("Insufficient stock: Cannot manufacture the product.");
            context.setFailureReason("Insufficient Stock");
            context.setState(new FailedState("Insufficient Stock"));
            context.incrementStockShortageCount();
            return;
        }

        int outcome = RandomGenerator.generateManufacturingOutcome();
        
        switch (outcome) {
            case 1:
                // Only use components stock on successful production
                if (context.getProduct().useComponentsStock()) {
                    System.out.println("The production is successful");
                    context.setState(new CompletedState());
                    context.incrementSuccessCount();
                } else {
                    // This should not happen if checkComponentsStock() is true
                    System.out.println("Unexpected error: Stock not available during production.");
                    context.setFailureReason("Stock Error");
                    context.setState(new FailedState("Unexpected Stock Error"));
                    context.incrementSystemErrorCount();
                }
                break;
            case 2:
                System.out.println("Production is failed: System error.");
                context.setFailureReason("System error");
                context.setState(new FailedState("System error"));
                context.incrementSystemErrorCount();
                break;
            case 3:
                System.out.println("Production is failed: Damaged component.");
                context.setFailureReason("Damaged component");
                context.setState(new FailedState("Damaged component"));
                context.incrementDamagedComponentCount();
                break;
        }
        
        // No need to call handle on the next state
        // context.getCurrentState().handle(context);
    }
    
    @Override
    public String getName() {
        return "Production Stage";
    }
} 