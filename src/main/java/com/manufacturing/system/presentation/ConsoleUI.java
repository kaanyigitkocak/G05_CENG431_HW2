package com.manufacturing.system.presentation;

import com.manufacturing.system.application.ManufacturingService;

/**
 * Kullanıcı arayüzünü sağlayan sınıf.
 * GRASP - Controller prensibine uygun olarak kullanıcı etkileşimini yönetir.
 */
public class ConsoleUI {
    private final ManufacturingService manufacturingService;
    
    public ConsoleUI() {
        this.manufacturingService = new ManufacturingService();
    }
    
    /**
     * Üretim sürecini başlatır ve sonuçları gösterir
     * @param componentsFilePath Bileşen dosya yolu
     * @param productsFilePath Ürün dosya yolu
     */
    public void startManufacturingProcess(String componentsFilePath, String productsFilePath) {
        try {
            System.out.println("======================================================");
            System.out.println("            ÜRETİM FİRMASI SİSTEMİ                   ");
            System.out.println("======================================================");
            
            // Application katmanındaki servis üzerinden üretim sürecini başlat
            String report = manufacturingService.startManufacturing(componentsFilePath, productsFilePath);
            
            // Özet raporu göster
            System.out.println(report);
            
            System.out.println("======================================================");
            System.out.println("            İŞLEM TAMAMLANDI                         ");
            System.out.println("======================================================");
        } catch (Exception e) {
            System.err.println("Üretim sürecinde hata oluştu: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 