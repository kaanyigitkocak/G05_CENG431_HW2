package com.manufacturing.system.domain.state;

import com.manufacturing.system.infrastructure.RandomGenerator;

/**
 * Üretim aşaması durumu.
 * Bu aşamada ürün üretilir, başarılı veya başarısız olabilir.
 */
public class InManufacturingState implements ManufacturingState {
    
    @Override
    public void handle(ManufacturingProcess context) {
        System.out.println("Üretim aşamasında...");
        
        // Bileşen stoklarını kullan
        if (!context.getProduct().useComponentsStock()) {
            // Bu duruma gelinmemeli, çünkü WaitingForStockState'de kontrol edildi
            System.out.println("Beklenmeyen hata: Stok kullanılamadı.");
            context.setFailureReason("Stok Hatası");
            context.setState(new FailedState("Beklenmeyen Stok Hatası"));
            context.incrementSystemErrorCount();
            return;
        }
        
        // Infrastructure katmanındaki RandomGenerator kullanarak rastgele değer al
        // 1: Başarılı, 2: Sistem Hatası, 3: Hasarlı Bileşen
        int outcome = RandomGenerator.generateManufacturingOutcome();
        
        switch (outcome) {
            case 1:
                System.out.println("Üretim başarılı.");
                context.setState(new CompletedState());
                context.incrementSuccessCount();
                break;
            case 2:
                System.out.println("Üretim başarısız: Sistem hatası.");
                context.setFailureReason("Sistem Hatası");
                context.setState(new FailedState("Sistem Hatası"));
                context.incrementSystemErrorCount();
                break;
            case 3:
                System.out.println("Üretim başarısız: Hasarlı bileşen.");
                context.setFailureReason("Hasarlı Bileşen");
                context.setState(new FailedState("Hasarlı Bileşen"));
                context.incrementDamagedComponentCount();
                break;
        }
        
        context.getCurrentState().handle(context);
    }
    
    @Override
    public String getName() {
        return "Üretim Aşaması";
    }
} 