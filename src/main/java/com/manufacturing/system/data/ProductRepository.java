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
        
        for (Map<String, String> record : records) {
            String id = record.get("id");
            String name = record.get("name");
            int quantity = Integer.parseInt(record.get("quantity"));
            
            Product product = new Product(id, name, 0);
            
            // Ürün bileşenlerini ekle (products.csv'deki format)
            String[] componentIds = record.get("component_ids").split(";");
            String[] componentQtys = record.get("component_quantities").split(";");
            
            for (int i = 0; i < componentIds.length; i++) {
                String componentId = componentIds[i].trim();
                int componentQty = Integer.parseInt(componentQtys[i].trim());
                
                Component component = componentMap.get(componentId);
                if (component != null) {
                    product.add(component, componentQty);
                } else {
                    System.out.println("Ürün " + id + " için bileşen bulunamadı: " + componentId);
                }
            }
            
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