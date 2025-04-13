package com.manufacturing.system;

import com.manufacturing.system.presentation.ConsoleUI;

/**
 * Üretim Firması Sistemi - Ana sınıf
 * GRASP, State ve Composite Design Pattern prensiplerine uygun olarak tasarlanmıştır.
 */
public class Application {
    public static void main(String[] args) {
        // Bileşen ve ürün dosya yolları
        String componentsFilePath = "src/main/resources/components.csv";
        String productsFilePath = "src/main/resources/products.csv";
        
        // Presentation katmanı üzerinden uygulamayı başlat
        ConsoleUI ui = new ConsoleUI();
        ui.startManufacturingProcess(componentsFilePath, productsFilePath);
    }
} 