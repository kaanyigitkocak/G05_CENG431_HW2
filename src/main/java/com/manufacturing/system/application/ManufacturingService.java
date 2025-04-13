package com.manufacturing.system.application;

import com.manufacturing.system.data.ComponentRepository;
import com.manufacturing.system.data.ProductRepository;
import com.manufacturing.system.domain.model.Component;
import com.manufacturing.system.domain.model.Product;
import com.manufacturing.system.domain.model.ProductionOrder;
import com.manufacturing.system.domain.state.ManufacturingProcess;
import com.manufacturing.system.presentation.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Üretim süreçlerini koordine eden servis sınıfı.
 * GRASP - Controller prensibine uygun olarak domain nesneleri arasındaki etkileşimi yönetir.
 */
public class ManufacturingService {
    private final ComponentRepository componentRepository;
    private final ProductRepository productRepository;
    private final Logger logger;
    private final List<ManufacturingProcess> completedProcesses = new ArrayList<>();
    
    public ManufacturingService(Logger logger) {
        this.logger = logger;
        this.componentRepository = new ComponentRepository("src/main/resources/components.csv", logger);
        this.productRepository = new ProductRepository("src/main/resources/products.csv", componentRepository, logger);
    }
    
    /**
     * Üretim sürecini başlatır
     * @return Üretim sonuç raporu
     */
    public String startManufacturing() {
        logger.log("Üretim Firması Sistemi başlatılıyor...");
        
        // Veri katmanından bileşenleri yükle
        logger.log("Bileşenler yükleniyor...");
        componentRepository.loadComponents();
        
        // Veri katmanından ürünleri yükle
        logger.log("Ürünler yükleniyor...");
        productRepository.loadProducts();
        
        // Üretim siparişlerini oluştur (Burada bir örnek oluşturuyoruz, gerçek uygulamada kullanıcı girişi veya dosyadan okunabilir)
        createSampleProductionOrders();
        
        logger.log("\n=== Üretim Başlıyor ===");
        
        // State Pattern kullanarak her ürün için üretim sürecini çalıştır
        List<ProductionOrder> orders = productRepository.getProductionOrders();
        for (ProductionOrder order : orders) {
            Product product = order.getProduct();
            int quantity = order.getQuantity();
            
            logger.log("\n--- " + product.getName() + " Üretimi (" + quantity + " adet) ---");
            
            ManufacturingProcess process = new ManufacturingProcess(product, quantity);
            process.run();
            
            completedProcesses.add(process);
            logger.log(process.generateReport());
        }
        
        logger.log("\n=== Üretim Tamamlandı ===");
        
        return generateSummaryReport();
    }
    
    /**
     * Örnek üretim siparişleri oluşturur.
     * Bu metot, ürünlerin tamamı için birer sipariş oluşturur.
     */
    private void createSampleProductionOrders() {
        List<Product> products = productRepository.getAll();
        
        logger.log("Üretim siparişleri oluşturuluyor...");
        
        // Her ürün için bir üretim siparişi oluştur
        for (Product product : products) {
            // Her üründen 1 adet üretilecek
            ProductionOrder order = new ProductionOrder(product, 1);
            productRepository.addProductionOrder(order);
            logger.log("Sipariş oluşturuldu: " + product.getName() + " - 1 adet");
        }
        
        logger.log("Toplam " + productRepository.getProductionOrders().size() + " sipariş oluşturuldu.");
    }
    
    /**
     * Tüm üretim süreçleri için özet rapor oluşturur
     * @return Özet rapor
     */
    public String generateSummaryReport() {
        int totalSuccess = 0;
        int totalSystemError = 0;
        int totalDamagedComponent = 0;
        int totalStockShortage = 0;
        double totalCost = 0;
        double totalWeight = 0;
        
        for (ManufacturingProcess process : completedProcesses) {
            totalSuccess += process.getSuccessCount();
            totalSystemError += process.getSystemErrorCount();
            totalDamagedComponent += process.getDamagedComponentCount();
            totalStockShortage += process.getStockShortageCount();
            
            if (process.getSuccessCount() > 0) {
                totalCost += process.getProduct().getCost() * process.getSuccessCount();
                totalWeight += process.getProduct().getWeight() * process.getSuccessCount();
            }
        }
        
        StringBuilder report = new StringBuilder();
        report.append("\n====== ÖZET RAPOR ======\n");
        report.append("Toplam Başarılı Üretim: ").append(totalSuccess).append(" adet\n");
        report.append("  - Toplam Maliyet: ").append(String.format("%.2f", totalCost)).append(" TL\n");
        report.append("  - Toplam Ağırlık: ").append(String.format("%.2f", totalWeight)).append(" kg\n");
        report.append("Toplam Başarısız Üretim: ").append(totalSystemError + totalDamagedComponent + totalStockShortage).append(" adet\n");
        report.append("  - Sistem Hatası: ").append(totalSystemError).append(" adet\n");
        report.append("  - Hasarlı Bileşen: ").append(totalDamagedComponent).append(" adet\n");
        report.append("  - Stok Yetersizliği: ").append(totalStockShortage).append(" adet\n");
        
        return report.toString();
    }
} 