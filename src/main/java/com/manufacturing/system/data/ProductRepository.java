package com.manufacturing.system.data;

import com.manufacturing.system.domain.model.*;
import com.manufacturing.system.presentation.Logger;
import com.manufacturing.system.util.CSVReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Ürün verilerini yönetmek için repository sınıfı.
 */
public class ProductRepository {
    private final List<Product> products;
    private final List<ProductionOrder> productionOrders;
    private final ComponentRepository componentRepository;
    private final Logger logger;
    private final String productsCsvPath;

    public ProductRepository(String productsCsvPath, ComponentRepository componentRepository, Logger logger) {
        this.products = new ArrayList<>();
        this.productionOrders = new ArrayList<>();
        this.componentRepository = componentRepository;
        this.logger = logger;
        this.productsCsvPath = productsCsvPath;
    }

    /**
     * CSV dosyasından ürünleri yükler.
     */
    public void loadProducts() {
        List<Map<String, String>> records = FileParser.parseCSV(productsCsvPath);
        if (records.isEmpty()) {
            logger.log("Uyarı: Ürün CSV dosyası boş veya okunamadı. Dosya yolu: " + productsCsvPath);
            return;
        }

        logger.log("Ürünler yükleniyor...");
        int successCount = 0;
        int errorCount = 0;

        for (Map<String, String> record : records) {
            try {
                // CSV dosyasından gerekli alanları al
                String name = record.get("Product Name");
                String quantityStr = record.get("Quantity");

                // Gerekli değerlerin varlığını kontrol et
                if (name == null || name.isEmpty()) {
                    logger.log("Hata: Ürün adı eksik - " + record);
                    errorCount++;
                    continue;
                }

                // Benzersiz ID oluştur (ürün adını kullanarak)
                String id = name.replaceAll("\\s+", "_").toLowerCase();

                // Stok miktarını dönüştür
                int quantity = 0;
                try {
                    if (quantityStr != null && !quantityStr.isEmpty()) {
                        quantity = Integer.parseInt(quantityStr.trim());
                    } else {
                        logger.log("Uyarı: Miktar değeri eksik, 0 kullanılacak - Ürün: " + name);
                    }
                } catch (NumberFormatException e) {
                    logger.log("Hata: Geçersiz miktar değeri '" + quantityStr + "' - Ürün: " + name + ". Hata: " + e.getMessage());
                    errorCount++;
                    continue;
                }

                // Yeni ürün oluştur
                Product product = new Product(id, name, 0);
                logger.log("Ürün oluşturuluyor: " + name + " (ID: " + id + ")");

                // CSV'deki tüm bileşenleri kontrol et ve ürüne ekle
                List<Component> allComponents = componentRepository.getAll();
                for (Component component : allComponents) {
                    String componentQuantityStr = record.get(component.getName());
                    if (componentQuantityStr != null && !componentQuantityStr.isEmpty() && !componentQuantityStr.equals("0")) {
                        try {
                            // Virgül yerine nokta kullanarak double parse et
                            double componentQuantityDouble = Double.parseDouble(componentQuantityStr.replace(',', '.'));
                            int componentQuantity = (int)Math.ceil(componentQuantityDouble); // 1,5 gibi değerleri yukarı yuvarla
                            
                            if (componentQuantity > 0) {
                                product.addComponent(component, componentQuantity);
                                logger.log("Bileşen eklendi: " + component.getName() + " (ID: " + component.getId() + ", Miktar: " + componentQuantity + ")");
                            }
                        } catch (NumberFormatException e) {
                            logger.log("Hata: Geçersiz bileşen miktar değeri '" + componentQuantityStr + "' - Bileşen: " + component.getName());
                        }
                    }
                }

                // Toplam maliyet ve ağırlığı hesapla ve göster
                logger.log("Ürün detayları: " + name + " - Toplam Maliyet: " + product.getCost() + " TL, Toplam Ağırlık: " + product.getWeight() + " kg");

                // Ürün listesine ekle
                products.add(product);
                successCount++;
            } catch (Exception e) {
                logger.log("Beklenmeyen hata: " + e.getMessage() + " kayıt işlenemedi: " + record);
                errorCount++;
            }
        }

        logger.log("Ürün yükleme tamamlandı. Başarılı: " + successCount + ", Hatalı: " + errorCount);
        logger.log("Toplam ürün sayısı: " + products.size());
    }

    public List<Product> getAll() {
        return products;
    }

    public Product findById(String id) {
        return products.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
    
    // Ürün adına göre ürün bulma metodu
    public Product findByName(String name) {
        return products.stream()
                .filter(p -> p.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public void addProductionOrder(ProductionOrder order) {
        productionOrders.add(order);
    }

    public List<ProductionOrder> getProductionOrders() {
        return productionOrders;
    }
} 