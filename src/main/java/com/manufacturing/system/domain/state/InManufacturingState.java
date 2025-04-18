package com.manufacturing.system.domain.state;

import com.manufacturing.system.infrastructure.RandomGenerator;

public class InManufacturingState implements ManufacturingState {
    
    @Override
    public void handle(ManufacturingProcess context) {
        System.out.println("During production...");

        if (!context.getProduct().useComponentsStock()) {
            System.out.println("Unexpected error: Stock not available.");
            context.setFailureReason("Stock Error");
            context.setState(new FailedState("Unexpected Stock Error"));
            context.incrementSystemErrorCount();
            return;
        }

        int outcome = RandomGenerator.generateManufacturingOutcome();
        
        switch (outcome) {
            case 1:
                System.out.println("The production is successful");
                context.setState(new CompletedState());
                context.incrementSuccessCount();
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
        
        context.getCurrentState().handle(context);
    }
    
    @Override
    public String getName() {
        return "Production Stage";
    }
} 