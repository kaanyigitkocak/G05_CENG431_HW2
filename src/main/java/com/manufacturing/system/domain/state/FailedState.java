package com.manufacturing.system.domain.state;

public class FailedState implements ManufacturingState {
    private final String reason;
    
    public FailedState(String reason) {
        this.reason = reason;
    }
    
    @Override
    public void handle(ManufacturingProcess context) {
        System.out.println("Production failed. Reason: " + reason);
    }
    
    @Override
    public String getName() {
        return "Failed: " + reason;
    }
    
    public String getReason() {
        return reason;
    }
} 