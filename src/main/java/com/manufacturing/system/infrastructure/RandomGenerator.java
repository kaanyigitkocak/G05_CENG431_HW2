package com.manufacturing.system.infrastructure;

import java.util.Random;

/**
 * Rastgele sayı üretimi için altyapı sınıfı.
 * GRASP - Indirection/Low Coupling prensiplerine uygun olarak
 * rastgele sayı üretimini sağlayan bir aracı sınıf.
 */
public class RandomGenerator {
    private static final Random random = new Random();
    
    private RandomGenerator() {
        // Utility sınıfını örneklemeyi engellemek için private constructor
    }
    
    /**
     * Belirtilen aralıkta rastgele sayı üretir
     * @param min Minimum değer (dahil)
     * @param max Maksimum değer (dahil)
     * @return Üretilen rastgele sayı
     */
    public static int generateNumber(int min, int max) {
        // min dahil, max dahil olacak şekilde değer üretir
        return min + random.nextInt(max - min + 1);
    }
    
    /**
     * Üretim sonuç durumunu rastgele belirler
     * @return 1: Başarılı, 2: Sistem Hatası, 3: Hasarlı Bileşen
     */
    public static int generateManufacturingOutcome() {
        return generateNumber(1, 3);
    }
    
    /**
     * Belirli bir olasılıkla başarı durumunu belirler
     * @param successProbability Başarı olasılığı (0.0 - 1.0 arası)
     * @return Başarılı ise true, değilse false
     */
    public static boolean isSuccessful(double successProbability) {
        return random.nextDouble() < successProbability;
    }
} 