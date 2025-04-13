package com.manufacturing.system.presentation;

/**
 * Sistem içerisinde günlük (log) işlemleri için kullanılan sınıf.
 */
public class Logger {
    private boolean debugMode;

    public Logger(boolean debugMode) {
        this.debugMode = debugMode;
    }

    /**
     * Sistem mesajını kaydeder ve ekrana yazdırır.
     * 
     * @param message Kaydedilecek mesaj
     */
    public void log(String message) {
        System.out.println(message);
    }

    /**
     * Hata mesajını kaydeder ve ekrana yazdırır.
     * 
     * @param message Kaydedilecek hata mesajı
     */
    public void error(String message) {
        System.err.println("HATA: " + message);
    }

    /**
     * Debug modunda ise mesajı kaydeder ve ekrana yazdırır.
     * 
     * @param message Kaydedilecek debug mesajı
     */
    public void debug(String message) {
        if (debugMode) {
            System.out.println("DEBUG: " + message);
        }
    }

    /**
     * Debug modunu ayarlar.
     * 
     * @param debugMode Debug modu durumu
     */
    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    /**
     * Debug modunun etkin olup olmadığını döndürür.
     * 
     * @return Debug modu durumu
     */
    public boolean isDebugMode() {
        return debugMode;
    }
} 