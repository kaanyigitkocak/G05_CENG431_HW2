package com.manufacturing.system.domain.state;

/**
 * Başarısız durum.
 * Üretim süreci başarısız olduğunda kullanılır.
 */
public class FailedState implements ManufacturingState {
    private final String reason;
    
    public FailedState(String reason) {
        this.reason = reason;
    }
    
    @Override
    public void handle(ManufacturingProcess context) {
        System.out.println("Üretim başarısız oldu. Neden: " + reason);
    }
    
    @Override
    public String getName() {
        return "Başarısız: " + reason;
    }
    
    public String getReason() {
        return reason;
    }
} 