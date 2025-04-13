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
        
        for (Map<String, String> record : records) {
            String id = record.get("id");
            String type = record.get("type");
            String name = record.get("name");
            double cost = Double.parseDouble(record.get("cost"));
            double weight = Double.parseDouble(record.get("weight"));
            int stock = Integer.parseInt(record.get("stock"));
            
            Component component;
            
            // Bileşen türüne göre uygun nesneyi oluştur
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
        
        return componentMap;
    }
} 