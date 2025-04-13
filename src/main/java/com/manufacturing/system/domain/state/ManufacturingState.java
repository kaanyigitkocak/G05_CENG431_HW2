package com.manufacturing.system.domain.state;

/**
 * State Design Pattern'de State arayüzü.
 * Üretim sürecinin farklı durumlarını temsil eder.
 */
public interface ManufacturingState {
    /**
     * Durumun işlevini gerçekleştirir ve gerekirse yeni duruma geçiş yapar
     * @param context Üretim süreci (context)
     */
    void handle(ManufacturingProcess context);
    
    /**
     * Durumun adını döndürür
     * @return Durum adı
     */
    String getName();
} 