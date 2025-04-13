package com.manufacturing.system.util;

import com.manufacturing.system.domain.model.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CSV dosyalarını okumak için yardımcı sınıf
 */
public class CSVReader {
    
    /**
     * Bileşenleri CSV dosyasından okur
     * @param filePath CSV dosya yolu
     * @return Okunan bileşenler
     */
    public static Map<String, Component> readComponents(String filePath) {
        Map<String, Component> componentMap = new HashMap<>();
        
        try (Reader reader = new FileReader(filePath);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {
            
            for (CSVRecord record : csvParser) {
                String id = record.get("id");
                String type = record.get("type");
                String name = record.get("name");
                double cost = Double.parseDouble(record.get("cost"));
                double weight = Double.parseDouble(record.get("weight"));
                int stock = Integer.parseInt(record.get("stock"));
                
                Component component;
                switch (type.toLowerCase()) {
                    case "raw_material":
                        component = new RawMaterial(id, name, cost, weight, stock);
                        break;
                    case "paint":
                        component = new Paint(id, name, cost, weight, stock);
                        break;
                    case "hardware":
                        component = new Hardware(id, name, cost, weight, stock);
                        break;
                    default:
                        System.out.println("Bilinmeyen bileşen türü: " + type);
                        continue;
                }
                
                componentMap.put(id, component);
            }
            
        } catch (IOException e) {
            System.err.println("Bileşenler okunurken hata oluştu: " + e.getMessage());
        }
        
        return componentMap;
    }
    
    /**
     * Ürünleri CSV dosyasından okur
     * @param filePath CSV dosya yolu
     * @param componentMap Daha önce okunan bileşenler
     * @return Ürün ve üretim miktarı listesi
     */
    public static List<ProductionOrder> readProducts(String filePath, Map<String, Component> componentMap) {
        List<ProductionOrder> orders = new ArrayList<>();
        
        try (Reader reader = new FileReader(filePath);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {
            
            for (CSVRecord record : csvParser) {
                String id = record.get("id");
                String name = record.get("name");
                int quantity = Integer.parseInt(record.get("quantity"));
                
                Product product = new Product(id, name, 0);
                
                // Ürün bileşenlerini ekle - bu kısım CSV formatına göre değişmeli
                // Örnek format: component_ids sütunu virgülle ayrılmış bileşen ID'lerini içerir
                // component_quantities sütunu virgülle ayrılmış miktarları içerir
                String[] componentIds = record.get("component_ids").split(",");
                String[] componentQtys = record.get("component_quantities").split(",");
                
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
            
        } catch (IOException e) {
            System.err.println("Ürünler okunurken hata oluştu: " + e.getMessage());
        }
        
        return orders;
    }
    
    /**
     * Ürün ve üretim miktarı bilgilerini tutan iç sınıf
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