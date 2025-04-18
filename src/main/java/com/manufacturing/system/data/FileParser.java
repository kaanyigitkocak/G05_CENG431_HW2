package com.manufacturing.system.data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileParser {
    
    /**
     * Reads CSV file and returns records.
     * 
     * @param filePath CSV file path
     * @return Mapped data list
     */
    public static List<Map<String, String>> parseCSV(String filePath) {
        List<Map<String, String>> records = new ArrayList<>();
        String line;
        String[] headers = null;
        String separator = ";";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            if ((line = br.readLine()) != null) {
                headers = line.split(separator);
                for (int i = 0; i < headers.length; i++) {
                    headers[i] = headers[i].trim();
                }
            }

            if (headers == null || headers.length == 0) {
                System.err.println("Unable to read CSV headers: " + filePath);
                return records;
            }

            System.out.println("CSV Headers: " + String.join(", ", headers));
            int rowCount = 0;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(separator);
                Map<String, String> record = new HashMap<>();
                for (int i = 0; i < headers.length && i < values.length; i++) {
                    String value = (i < values.length && values[i] != null) ? values[i].trim() : "";
                    if (value.contains(",")) {
                        try {
                            String trimmed = value.trim();
                            if (trimmed.matches("[0-9,]+")) {
                                value = trimmed.replace(",", ".");
                            }
                        } catch (Exception e) {
                            System.out.println("Decimal Conversion Error: " + value + " - " + e.getMessage());
                        }
                    }
                    
                    record.put(headers[i], value);
                }

                records.add(record);
                rowCount++;

                if (rowCount == 1) {
                    System.out.println("First CSV Record:");
                    for (Map.Entry<String, String> entry : record.entrySet()) {
                        System.out.println("  " + entry.getKey() + " = '" + entry.getValue() + "'");
                    }
                }
            }

            System.out.println("Total " + rowCount + " record read - " + filePath);

        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage() + " - File: " + filePath);
        }

        return records;
    }
} 