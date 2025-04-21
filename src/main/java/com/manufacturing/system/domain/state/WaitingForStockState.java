package com.manufacturing.system.domain.state;


public class WaitingForStockState implements ManufacturingState {
    
    @Override
    public void handle(ManufacturingProcess context) {
        System.out.println("Stock control is being carried out...");
        
        if (context.getProduct().checkComponentsStock()) {
            System.out.println("The stock is sufficient. The production phase is starting.");
            context.setState(new InManufacturingState());
            context.getCurrentState().handle(context);
        } else {
            System.out.println("Insufficient stock. Production failure.");
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