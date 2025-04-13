package com.manufacturing.system.application;

import com.manufacturing.system.data.ComponentRepository;
import com.manufacturing.system.data.ProductRepository;
import com.manufacturing.system.data.ProductRepository.ProductionOrder;
import com.manufacturing.system.domain.model.Component;
import com.manufacturing.system.domain.model.Product;
import com.manufacturing.system.domain.state.ManufacturingProcess;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Üretim süreçlerini koordine eden servis sınıfı.
 * GRASP - Controller prensibine uygun olarak domain nesneleri arasındaki etkileşimi yönetir.
 */
public class ManufacturingService {
    private final ComponentRepository componentRepository;
    private final ProductRepository productRepository;
    private final List<ManufacturingProcess> completedProcesses = new ArrayList<>();
    
    public ManufacturingService() {
        this.componentRepository = new ComponentRepository();
        this.productRepository = new ProductRepository();
    }
    
    /**
     * Üretim sürecini başlatır
     * @param componentsFilePath Bileşen dosya yolu
     * @param productsFilePath Ürün dosya yolu
     * @return Üretim sonuç raporu
     */
    public String startManufacturing(String componentsFilePath, String productsFilePath) {
        System.out.println("Üretim Firması Sistemi başlatılıyor...");
        
        // Veri katmanından bileşenleri yükle
        System.out.println("Bileşenler yükleniyor...");
        Map<String, Component> components = componentRepository.loadComponents(componentsFilePath);
        System.out.println(components.size() + " bileşen yüklendi.");
        
        // Veri katmanından ürünleri yükle
        System.out.println("Ürünler yükleniyor...");
        List<ProductionOrder> orders = productRepository.loadProducts(productsFilePath, components);
        System.out.println(orders.size() + " ürün yüklendi.");
        
        System.out.println("\n=== Üretim Başlıyor ===");
        
        // State Pattern kullanarak her ürün için üretim sürecini çalıştır
        for (ProductionOrder order : orders) {
            Product product = order.getProduct();
            int quantity = order.getQuantity();
            
            System.out.println("\n--- " + product.getName() + " Üretimi (" + quantity + " adet) ---");
            
            ManufacturingProcess process = new ManufacturingProcess(product, quantity);
            process.run();
            
            completedProcesses.add(process);
            System.out.println(process.generateReport());
        }
        
        System.out.println("\n=== Üretim Tamamlandı ===");
        
        return generateSummaryReport();
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
        report.append("  - Toplam Maliyet: ").append(String.format("%.2f", totalCost)).append("\n");
        report.append("  - Toplam Ağırlık: ").append(String.format("%.2f", totalWeight)).append("\n");
        report.append("Toplam Başarısız Üretim: ").append(totalSystemError + totalDamagedComponent + totalStockShortage).append(" adet\n");
        report.append("  - Sistem Hatası: ").append(totalSystemError).append(" adet\n");
        report.append("  - Hasarlı Bileşen: ").append(totalDamagedComponent).append(" adet\n");
        report.append("  - Stok Yetersizliği: ").append(totalStockShortage).append(" adet\n");
        
        return report.toString();
    }
} 