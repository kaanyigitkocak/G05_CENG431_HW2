package com.manufacturing.system.domain.model;

/**
 * Composite Pattern'de Component arayüzü.
 * Tüm bileşenler (hammadde, boya, donanım ve ürünler) bu arayüzü uygular.
 */
public interface Component {
    /**
     * Bileşenin maliyetini hesaplar
     * @return Maliyet değeri
     */
    double getCost();
    
    /**
     * Bileşenin ağırlığını hesaplar
     * @return Ağırlık değeri
     */
    double getWeight();
    
    /**
     * Bileşenin adını döndürür
     * @return Bileşen adı
     */
    String getName();
    
    /**
     * Bileşenin stok miktarını döndürür
     * @return Stok miktarı
     */
    int getStock();
    
    /**
     * Bileşeni rapor formatında döndürür
     * @return Rapor çıktısı
     */
    String generateReport();
    
    /**
     * Başka bir bileşeni ekler (varsayılan olarak desteklenmez)
     * Sadece Composite nesneler (Product) bu metodu override eder
     * @param component Eklenecek bileşen
     */
    default void add(Component component) {
        throw new UnsupportedOperationException(getName() + " bileşenine alt bileşen eklenemez");
    }
    
    /**
     * Bileşenden belirtilen miktarda stok kullanır
     * @param quantity Kullanılacak miktar
     * @return Stok kullanımı başarılı ise true, değilse false
     */
    boolean useStock(int quantity);
} 