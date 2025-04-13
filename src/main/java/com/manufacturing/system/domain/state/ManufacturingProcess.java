package com.manufacturing.system.domain.state;

import com.manufacturing.system.domain.model.Product;

/**
 * State Design Pattern'de Context sınıfı.
 * Üretim sürecinin durumunu tutar ve durumlar arası geçişi sağlar.
 */
public class ManufacturingProcess {
    private ManufacturingState currentState;
    private final Product product;
    private final int quantity;
    private String failureReason;
    
    // Üretim sonucu istatistikleri
    private int successCount = 0;
    private int systemErrorCount = 0;
    private int damagedComponentCount = 0;
    private int stockShortageCount = 0;
    
    public ManufacturingProcess(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        // Başlangıç durumu: Stok için beklemede
        this.currentState = new WaitingForStockState();
    }
    
    /**
     * Üretim sürecini yürütür
     */
    public void run() {
        for (int i = 0; i < quantity; i++) {
            System.out.println("\n" + product.getName() + " üretimi başlıyor (" + (i + 1) + "/" + quantity + ")...");
            // Her üretim işlemi için durumu WaitingForStock olarak başlat
            this.currentState = new WaitingForStockState();
            // Durum işleyicisini çalıştır
            this.currentState.handle(this);
        }
    }
    
    /**
     * Mevcut durumu değiştirir
     * @param state Yeni durum
     */
    public void setState(ManufacturingState state) {
        this.currentState = state;
        System.out.println("Durum değişti: " + state.getName());
    }
    
    /**
     * Mevcut durumu döndürür
     * @return Mevcut durum
     */
    public ManufacturingState getCurrentState() {
        return currentState;
    }
    
    /**
     * Üretilecek ürünü döndürür
     * @return Ürün
     */
    public Product getProduct() {
        return product;
    }
    
    /**
     * Başarılı üretim sayısını artırır
     */
    public void incrementSuccessCount() {
        successCount++;
    }
    
    /**
     * Sistem hatası sayısını artırır
     */
    public void incrementSystemErrorCount() {
        systemErrorCount++;
    }
    
    /**
     * Hasarlı bileşen sayısını artırır
     */
    public void incrementDamagedComponentCount() {
        damagedComponentCount++;
    }
    
    /**
     * Stok yetersizliği sayısını artırır
     */
    public void incrementStockShortageCount() {
        stockShortageCount++;
    }
    
    /**
     * Başarısızlık nedenini belirler
     * @param reason Başarısızlık nedeni
     */
    public void setFailureReason(String reason) {
        this.failureReason = reason;
    }
    
    /**
     * Başarısızlık nedenini döndürür
     * @return Başarısızlık nedeni
     */
    public String getFailureReason() {
        return failureReason;
    }
    
    /**
     * Üretim istatistiklerini rapor olarak döndürür
     * @return Rapor metni
     */
    public String generateReport() {
        StringBuilder report = new StringBuilder();
        report.append("\n==== ").append(product.getName()).append(" Üretim Raporu ====\n");
        report.append("Üretim İsteği: ").append(quantity).append(" adet\n");
        report.append("Başarılı Üretim: ").append(successCount).append(" adet\n");
        
        if (successCount > 0) {
            report.append("  - Birim Maliyet: ").append(String.format("%.2f", product.getCost())).append("\n");
            report.append("  - Birim Ağırlık: ").append(String.format("%.2f", product.getWeight())).append("\n");
            report.append("  - Toplam Maliyet: ").append(String.format("%.2f", product.getCost() * successCount)).append("\n");
            report.append("  - Toplam Ağırlık: ").append(String.format("%.2f", product.getWeight() * successCount)).append("\n");
        }
        
        report.append("Başarısız Üretim: ").append(systemErrorCount + damagedComponentCount + stockShortageCount).append(" adet\n");
        
        if (systemErrorCount > 0) {
            report.append("  - Sistem Hatası: ").append(systemErrorCount).append(" adet\n");
        }
        
        if (damagedComponentCount > 0) {
            report.append("  - Hasarlı Bileşen: ").append(damagedComponentCount).append(" adet\n");
        }
        
        if (stockShortageCount > 0) {
            report.append("  - Stok Yetersizliği: ").append(stockShortageCount).append(" adet\n");
        }
        
        return report.toString();
    }
    
    /**
     * Tüm istatistiklerin toplamını döndürür
     * @return Toplam sayı
     */
    public int getTotalProcessedCount() {
        return successCount + systemErrorCount + damagedComponentCount + stockShortageCount;
    }
    
    /**
     * Başarılı üretim sayısını döndürür
     * @return Başarılı üretim sayısı
     */
    public int getSuccessCount() {
        return successCount;
    }
    
    /**
     * Sistem hatası sayısını döndürür
     * @return Sistem hatası sayısı
     */
    public int getSystemErrorCount() {
        return systemErrorCount;
    }
    
    /**
     * Hasarlı bileşen sayısını döndürür
     * @return Hasarlı bileşen sayısı
     */
    public int getDamagedComponentCount() {
        return damagedComponentCount;
    }
    
    /**
     * Stok yetersizliği sayısını döndürür
     * @return Stok yetersizliği sayısı
     */
    public int getStockShortageCount() {
        return stockShortageCount;
    }
} 