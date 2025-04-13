package com.manufacturing.system.data;

import com.manufacturing.system.domain.model.*;
import com.manufacturing.system.presentation.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Component nesnelerini depolamak ve yönetmek için kullanılan depo sınıfı.
 */
public class ComponentRepository {
    private final List<Component> components;
    private final Logger logger;
    private final String componentsCsvPath;

    public ComponentRepository(String componentsCsvPath, Logger logger) {
        this.components = new ArrayList<>();
        this.logger = logger;
        this.componentsCsvPath = componentsCsvPath;
    }

    /**
     * CSV dosyasından bileşenleri yükler.
     */
    public void loadComponents() {
        List<Map<String, String>> records = FileParser.parseCSV(componentsCsvPath);
        if (records.isEmpty()) {
            logger.log("Uyarı: Bileşen CSV dosyası boş veya okunamadı. Dosya yolu: " + componentsCsvPath);
            return;
        }

        logger.log("Bileşenler yükleniyor...");
        int successCount = 0;
        int errorCount = 0;

        for (Map<String, String> record : records) {
            try {
                // CSV dosyasından gerekli alanları al
                String name = record.get("Component");
                String costStr = record.get("Unit Cost (TL)");
                String weightStr = record.get("Unit Weight (kg)");
                String type = record.get("Type");
                String stockStr = record.get("Stock Quantity");

                // Gerekli değerlerin varlığını kontrol et
                if (name == null || name.isEmpty()) {
                    logger.log("Hata: Bileşen adı eksik");
                    errorCount++;
                    continue;
                }
                if (type == null || type.isEmpty()) {
                    logger.log("Hata: Bileşen tipi eksik - Bileşen: " + name);
                    errorCount++;
                    continue;
                }

                // Benzersiz ID oluştur (bileşen adını kullanarak)
                String id = name.replaceAll("\\s+", "_").toLowerCase();

                // Sayısal değerleri dönüştür
                double cost = 0.0;
                try {
                    if (costStr != null && !costStr.isEmpty()) {
                        cost = Double.parseDouble(costStr);
                    } else {
                        logger.log("Uyarı: Maliyet değeri eksik, 0 kullanılacak - Bileşen: " + name);
                    }
                } catch (NumberFormatException e) {
                    logger.log("Hata: Geçersiz maliyet değeri '" + costStr + "' - Bileşen: " + name + ". Hata: " + e.getMessage());
                    errorCount++;
                    continue;
                }

                float weight = 0.0f;
                try {
                    if (weightStr != null && !weightStr.isEmpty()) {
                        weight = Float.parseFloat(weightStr);
                    } else {
                        logger.log("Uyarı: Ağırlık değeri eksik, 0 kullanılacak - Bileşen: " + name);
                    }
                } catch (NumberFormatException e) {
                    logger.log("Hata: Geçersiz ağırlık değeri '" + weightStr + "' - Bileşen: " + name + ". Hata: " + e.getMessage());
                    errorCount++;
                    continue;
                }

                // Stok değerini sayısal kısmını çıkar
                int stock = 0;
                try {
                    if (stockStr != null && !stockStr.isEmpty()) {
                        // Stok değeri "1000 m²" gibi bir formatta olabilir, sadece sayısal kısmı al
                        String stockNumeric = stockStr.split("\\s+")[0];
                        stock = Integer.parseInt(stockNumeric);
                    } else {
                        logger.log("Uyarı: Stok değeri eksik, 0 kullanılacak - Bileşen: " + name);
                    }
                } catch (Exception e) {
                    logger.log("Hata: Geçersiz stok değeri '" + stockStr + "' - Bileşen: " + name + ". Hata: " + e.getMessage());
                    errorCount++;
                    continue;
                }

                // Tip kontrolü ve bileşen oluşturma
                Component component;
                switch (type.toLowerCase().trim()) {
                    case "rawmaterial":
                    case "raw material":
                        component = new RawMaterial(id, name, cost, weight, stock);
                        logger.log("Hammadde eklendi: " + name + " (ID: " + id + ", Maliyet: " + cost + ", Ağırlık: " + weight + ", Stok: " + stock + ")");
                        break;
                    case "paint":
                        component = new Paint(id, name, cost, weight, stock);
                        logger.log("Boya eklendi: " + name + " (ID: " + id + ", Maliyet: " + cost + ", Ağırlık: " + weight + ", Stok: " + stock + ")");
                        break;
                    case "hardware":
                        component = new Hardware(id, name, cost, weight, stock);
                        logger.log("Donanım eklendi: " + name + " (ID: " + id + ", Maliyet: " + cost + ", Ağırlık: " + weight + ", Stok: " + stock + ")");
                        break;
                    default:
                        logger.log("Hata: Bilinmeyen bileşen tipi: " + type + " - Bileşen: " + name);
                        errorCount++;
                        continue;
                }

                components.add(component);
                successCount++;
            } catch (Exception e) {
                logger.log("Beklenmeyen hata: " + e.getMessage() + " kayıt işlenemedi: " + record);
                errorCount++;
            }
        }

        logger.log("Bileşen yükleme tamamlandı. Başarılı: " + successCount + ", Hatalı: " + errorCount);
        logger.log("Toplam bileşen sayısı: " + components.size());
    }

    public List<Component> getAll() {
        return components;
    }

    public Component findById(String id) {
        return components.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
    
    // Bileşen adına göre bileşen bulma metodu
    public Component findByName(String name) {
        return components.stream()
                .filter(c -> c.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
} 