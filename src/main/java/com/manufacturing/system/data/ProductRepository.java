package com.manufacturing.system.data;

import com.manufacturing.system.domain.model.Component;
import com.manufacturing.system.domain.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Ürün verilerini yönetmek için repository sınıfı.
 * GRASP - Creator prensibine uygun olarak ürün nesnelerini oluşturur.
 */
public class ProductRepository {

    /**
     * CSV dosyasından ürünleri okur ve ilgili nesnelere dönüştürür
     * @param filePath CSV dosya yolu
     * @param componentMap Bileşen referansları için gereken bileşen haritası
     * @return Ürünleri ve adetlerini içeren Production emir listesi
     */
    public List<ProductionOrder> loadProducts(String filePath, Map<String, Component> componentMap) {
        List<ProductionOrder> orders = new ArrayList<>();
        List<Map<String, String>> records = FileParser.parseCSV(filePath);
        
        if (records.isEmpty()) {
            System.out.println("Ürün CSV dosyası boş veya okunamadı!");
            return orders;
        }
        
        // ComponentMap anahtarlarını yazdır - debug
        System.out.println("ComponentMap anahtarları: " + componentMap.keySet());
        
        for (Map<String, String> record : records) {
            String productName = record.get("Product Name");
            if (productName == null || productName.isEmpty()) {
                continue; // Ürün adı yoksa atla
            }
            
            System.out.println("\nÜrün yükleniyor: " + productName);
            System.out.println("Kayıt anahtarları: " + record.keySet());
            
            // Ürün miktarını al
            int quantity = 0;
            try {
                quantity = Integer.parseInt(record.get("Quantity"));
            } catch (NumberFormatException e) {
                System.out.println("Miktar değeri okunamadı: " + record.get("Quantity"));
                continue;
            }
            
            // Ürünü oluştur
            String productId = productName.toLowerCase().replace(" ", "_");
            Product product = new Product(productId, productName, 0);
            
            int bilesenSayisi = 0;
            
            // Tüm bileşenleri dolaş ve ürüne ekle
            for (String componentName : componentMap.keySet()) {
                if (record.containsKey(componentName)) {
                    String quantityStr = record.get(componentName);
                    if (quantityStr != null && !quantityStr.isEmpty() && !quantityStr.equals("0")) {
                        try {
                            double componentQty = Double.parseDouble(quantityStr);
                            if (componentQty > 0) {
                                Component component = componentMap.get(componentName);
                                if (component != null) {
                                    // Ondalıklı miktarları tam sayıya yuvarla
                                    int intQuantity = (int)Math.ceil(componentQty);
                                    product.add(component, intQuantity);
                                    
                                    // Debug bilgisi
                                    System.out.println("  - Bileşen eklendi: " + componentName + 
                                                      " (" + intQuantity + " adet)" +
                                                      " Maliyet: " + component.getCost() + 
                                                      " Ağırlık: " + component.getWeight());
                                    bilesenSayisi++;
                                } else {
                                    System.out.println("  - HATA: Bileşen bulunamadı: " + componentName);
                                }
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("  - HATA: Bileşen miktarı okunamadı: " + componentName + " - " + quantityStr);
                        }
                    }
                }
            }
            
            // Ürün toplam maliyet ve ağırlık kontrolü
            System.out.println("  Ürün toplam bileşen sayısı: " + bilesenSayisi);
            System.out.println("  Ürün toplam maliyet: " + product.getCost());
            System.out.println("  Ürün toplam ağırlık: " + product.getWeight());
            
            orders.add(new ProductionOrder(product, quantity));
        }
        
        return orders;
    }
    
    /**
     * Ürün ve üretim miktarı bilgilerini tutan sınıf.
     * Data Transfer Object (DTO) görevi görür.
     */
    public static class ProductionOrder {
        private final Product product;
        private final int quantity;
        
        public ProductionOrder(Product product, int quantity) {
            this.product = product;
            this.quantity = quantity;
        }
        
        public Product getProduct() {
            return product;
        }
        
        public int getQuantity() {
            return quantity;
        }
    }
} 