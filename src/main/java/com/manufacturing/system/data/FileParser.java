package com.manufacturing.system.data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CSV dosyalarını okumak için kullanılan yardımcı sınıf.
 * Apache Commons CSV yerine standart Java kullanarak CSV dosyalarını okur.
 */
public class FileParser {

    /**
     * CSV dosyasından veri satırlarını okur
     * @param filePath Dosya yolu
     * @return Başlık (header) ve veri satırlarını içeren liste
     */
    public static List<Map<String, String>> parseCSV(String filePath) {
        List<Map<String, String>> records = new ArrayList<>();
        String line;
        String[] headers = null;
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            // İlk satırı başlık olarak oku
            if ((line = br.readLine()) != null) {
                headers = line.split(";");
            }
            
            // Diğer satırları oku
            while ((line = br.readLine()) != null) {
                String[] values = line.split(";");
                Map<String, String> record = new HashMap<>();
                
                // Başlıklar ve değerleri eşleştir
                for (int i = 0; i < headers.length; i++) {
                    if (i < values.length) {
                        String value = values[i].trim();
                        // Ondalık sayılarda virgül yerine nokta kullan
                        if (value.contains(",")) {
                            value = value.replace(",", ".");
                        }
                        record.put(headers[i].trim(), value);
                    } else {
                        record.put(headers[i].trim(), ""); // Eksik değerler için boş değer ekle
                    }
                }
                
                records.add(record);
            }
        } catch (IOException e) {
            System.err.println("Dosya okuma hatası: " + e.getMessage());
        }
        
        return records;
    }
} 