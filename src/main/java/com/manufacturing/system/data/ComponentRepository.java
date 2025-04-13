package com.manufacturing.system.data;

import com.manufacturing.system.domain.model.Component;
import com.manufacturing.system.domain.model.Hardware;
import com.manufacturing.system.domain.model.Paint;
import com.manufacturing.system.domain.model.RawMaterial;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Bileşen verilerini yönetmek için repository sınıfı.
 * GRASP - Creator prensibine uygun olarak bileşen nesnelerini oluşturur.
 */
public class ComponentRepository {

    /**
     * CSV dosyasından bileşenleri okur ve ilgili nesnelere dönüştürür
     * @param filePath CSV dosya yolu
     * @return Bileşen ID'si ve bileşen nesnelerini içeren harita
     */
    public Map<String, Component> loadComponents(String filePath) {
        Map<String, Component> componentMap = new HashMap<>();
        List<Map<String, String>> records = FileParser.parseCSV(filePath);
        
        System.out.println("CSV başlıkları: " + records.get(0).keySet());
        
        for (Map<String, String> record : records) {
            // Yeni CSV dosya formatına göre alanlar
            String name = record.get("Component");
            if (name == null || name.isEmpty()) {
                System.out.println("Bileşen adı bulunamadı. Kayıt atlanıyor: " + record);
                continue;
            }
            
            // Diğer alanları kontrol et ve dönüştür
            String costStr = record.get("Unit Cost (TL)");
            String weightStr = record.get("Unit Weight (kg)");
            String type = record.get("Type");
            String stockStr = record.get("Stock Quantity");
            
            if (costStr == null || weightStr == null || type == null || stockStr == null) {
                System.out.println("Eksik veri. Kayıt atlanıyor: " + record);
                continue;
            }
            
            try {
                double cost = Double.parseDouble(costStr);
                double weight = Double.parseDouble(weightStr);
                
                // Stock Quantity içindeki sayısal olmayan karakterleri temizle
                int stock = 0;
                try {
                    stockStr = stockStr.replaceAll("[^0-9]", "").trim();
                    if (!stockStr.isEmpty()) {
                        stock = Integer.parseInt(stockStr);
                    }
                } catch (Exception e) {
                    System.out.println("Stok değeri okunamadı: " + stockStr + ", 0 olarak ayarlanıyor");
                }
                
                // Her bileşene, adını kullanarak bir ID atayalım
                String id = name.toLowerCase().replace(" ", "_");
                
                Component component;
                
                // Bileşen türüne göre uygun nesneyi oluştur
                switch (type.toLowerCase()) {
                    case "raw material":
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
                
                componentMap.put(name, component); // Bileşen haritasına ekle (ürün CSV'si için ad kullanılacak)
                System.out.println("Bileşen eklendi: " + name);
            } catch (NumberFormatException e) {
                System.out.println("Sayısal değer okunamadı. Kayıt atlanıyor: " + record + " Hata: " + e.getMessage());
            }
        }
        
        return componentMap;
    }
} 