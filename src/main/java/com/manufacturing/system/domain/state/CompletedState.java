package com.manufacturing.system.domain.state;

/**
 * Tamamlanmış durum.
 * Üretim süreci başarıyla tamamlandığında kullanılır.
 */
public class CompletedState implements ManufacturingState {
    
    @Override
    public void handle(ManufacturingProcess context) {
        System.out.println("Üretim başarıyla tamamlandı.");
    }
    
    @Override
    public String getName() {
        return "Tamamlandı";
    }
} 