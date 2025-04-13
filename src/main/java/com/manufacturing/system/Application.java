package com.manufacturing.system;

import com.manufacturing.system.presentation.ConsoleUI;

/**
 * Üretim Firması Sistemi - Ana sınıf
 * GRASP, State ve Composite Design Pattern prensiplerine uygun olarak tasarlanmıştır.
 */
public class Application {
    public static void main(String[] args) {
        // Presentation katmanı üzerinden uygulamayı başlat
        ConsoleUI ui = new ConsoleUI();
        ui.startManufacturingProcess();
    }
} 