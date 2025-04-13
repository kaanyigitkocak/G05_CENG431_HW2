package com.manufacturing.system.domain.model;

/**
 * Ürün ve üretim miktarı bilgilerini tutan sınıf.
 * Data Transfer Object (DTO) görevi görür.
 */
public class ProductionOrder {
    private final Product product;
    private final int quantity;
    private boolean completed;
    
    public ProductionOrder(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        this.completed = false;
    }

    public Product getProduct() {
        return product;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public boolean isCompleted() {
        return completed;
    }
    
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
    
    /**
     * Üretim emrinin toplam maliyetini hesaplar.
     * 
     * @return Toplam maliyet
     */
    public double getTotalCost() {
        return product.getCost() * quantity;
    }
    
    /**
     * Üretim emrinin toplam ağırlığını hesaplar.
     * 
     * @return Toplam ağırlık
     */
    public double getTotalWeight() {
        return product.getWeight() * quantity;
    }
    
    @Override
    public String toString() {
        return String.format("Üretim Emri: %s - %d adet, Birim Maliyet: %.2f TL, Birim Ağırlık: %.2f kg, " +
                            "Toplam Maliyet: %.2f TL, Toplam Ağırlık: %.2f kg, Durum: %s",
                            product.getName(), quantity, product.getCost(), product.getWeight(),
                            getTotalCost(), getTotalWeight(), completed ? "Tamamlandı" : "Bekliyor");
    }
} 