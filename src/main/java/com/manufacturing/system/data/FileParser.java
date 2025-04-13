package com.manufacturing.system.data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CSV dosyalarını okumak için yardımcı sınıf.
 */
public class FileParser {
    
    /**
     * CSV dosyasını okur ve kayıtları döndürür.
     * Türkçe decimal format (virgül ile yazılan) otomatik olarak İngilizce formata (nokta ile yazılan) dönüştürülür.
     * 
     * @param filePath CSV dosya yolu
     * @return Kayıtlar listesi (her kayıt bir Map olarak anahtar-değer çiftleri içerir)
     */
    public static List<Map<String, String>> parseCSV(String filePath) {
        List<Map<String, String>> records = new ArrayList<>();
        String line;
        String[] headers = null;
        String separator = ";"; // CSV ayırıcı karakteri

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            // İlk satırı oku (başlıklar)
            if ((line = br.readLine()) != null) {
                headers = line.split(separator);
                // Başlıkları temizle
                for (int i = 0; i < headers.length; i++) {
                    headers[i] = headers[i].trim();
                }
            }

            // Başlık yoksa boş liste döndür
            if (headers == null || headers.length == 0) {
                System.err.println("CSV başlıkları okunamadı: " + filePath);
                return records;
            }

            // Debug için başlıkları yazdır
            System.out.println("CSV başlıkları: " + String.join(", ", headers));

            // Veri satırlarını oku
            int rowCount = 0;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(separator);
                Map<String, String> record = new HashMap<>();

                // Her bir değeri ilgili başlık ile eşleştir
                for (int i = 0; i < headers.length && i < values.length; i++) {
                    // Değer varsa temizle ve kaydet
                    String value = (i < values.length && values[i] != null) ? values[i].trim() : "";
                    
                    // Türkçe ondalık formatını (1,23) İngilizce formata (1.23) dönüştür
                    if (value.contains(",")) {
                        // Sayısal değer olma ihtimali var, virgülü noktaya çevir
                        try {
                            // Önce boşlukları temizle
                            String trimmed = value.trim();
                            // Eğer sadece sayı ve virgül içeriyorsa dönüştür
                            if (trimmed.matches("[0-9,]+")) {
                                value = trimmed.replace(",", ".");
                            }
                        } catch (Exception e) {
                            // Dönüştürme hatası olursa orijinal değeri kullan
                            System.out.println("Ondalık dönüştürme hatası: " + value + " - " + e.getMessage());
                        }
                    }
                    
                    record.put(headers[i], value);
                }

                records.add(record);
                rowCount++;

                // İlk kaydı debug için yazdır
                if (rowCount == 1) {
                    System.out.println("İlk CSV kaydı:");
                    for (Map.Entry<String, String> entry : record.entrySet()) {
                        System.out.println("  " + entry.getKey() + " = '" + entry.getValue() + "'");
                    }
                }
            }

            System.out.println("Toplam " + rowCount + " kayıt okundu - " + filePath);

        } catch (IOException e) {
            System.err.println("CSV dosyası okuma hatası: " + e.getMessage() + " - Dosya: " + filePath);
        }

        return records;
    }
} 