package com.manufacturing.system.domain.state;

/**
 * Stok için bekleme durumu.
 * Üretim sürecinin ilk durumudur.
 */
public class WaitingForStockState implements ManufacturingState {
    
    @Override
    public void handle(ManufacturingProcess context) {
        System.out.println("Stok kontrolü yapılıyor...");
        
        // Ürünün bileşenlerinin stok kontrolü
        if (context.getProduct().checkComponentsStock()) {
            System.out.println("Stok yeterli. Üretim aşamasına geçiliyor.");
            context.setState(new InManufacturingState());
            context.getCurrentState().handle(context);
        } else {
            System.out.println("Stok yetersiz. Üretim başarısız.");
            context.setFailureReason("Stok Yetersizliği");
            context.setState(new FailedState("Stok Yetersizliği"));
            context.incrementStockShortageCount();
            context.getCurrentState().handle(context);
        }
    }
    
    @Override
    public String getName() {
        return "Stok Kontrolü";
    }
} 