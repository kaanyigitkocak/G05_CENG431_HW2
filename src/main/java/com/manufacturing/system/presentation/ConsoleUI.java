package com.manufacturing.system.presentation;

import com.manufacturing.system.application.ManufacturingService;

/**
 * Kullanıcı arayüzü sınıfı.
 * GRASP - Controller prensiplerini destekler, domain katmanını sunum katmanından ayırır.
 */
public class ConsoleUI {
    private final Logger logger;
    private final ManufacturingService manufacturingService;

    public ConsoleUI() {
        // Debug modu kapalı olarak bir logger nesnesi oluştur
        this.logger = new Logger(false);
        // Logger kullanarak ManufacturingService örneği oluştur
        this.manufacturingService = new ManufacturingService(logger);
    }

    /**
     * Üretim sürecini başlatır ve sonuçları görüntüler
     */
    public void startManufacturingProcess() {
        // Konsola başlık yaz
        logger.log("\n========== ÜRETİM FİRMASI SİSTEMİ ==========\n");
        
        // ManufacturingService üzerinden üretim sürecini başlat
        String report = manufacturingService.startManufacturing();
        
        // Üretim raporu çıktısını göster
        logger.log(report);
        
        // Program sonunu belirt
        logger.log("\n========== PROGRAM SONLANDI ==========");
    }
} 