package com.manufacturing.system.domain.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Ürün bileşeni (Composite)
 * Hammadde, boya ve donanım gibi alt bileşenleri içerebilir
 */
public class Product implements Component {
    private final String id;
    private final String name;
    private int stock;
    private final List<ComponentQuantity> components = new ArrayList<>();
    
    public Product(String id, String name, int stock) {
        this.id = id;
        this.name = name;
        this.stock = stock;
    }
    
    @Override
    public void add(Component component) {
        add(component, 1);
    }
    
    public void add(Component component, int quantity) {
        components.add(new ComponentQuantity(component, quantity));
    }
    
    @Override
    public double getCost() {
        double totalCost = 0;
        for (ComponentQuantity cq : components) {
            totalCost += cq.getTotalCost();
        }
        return totalCost;
    }
    
    @Override
    public double getWeight() {
        double totalWeight = 0;
        for (ComponentQuantity cq : components) {
            totalWeight += cq.getTotalWeight();
        }
        return totalWeight;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    public String getId() {
        return id;
    }
    
    @Override
    public int getStock() {
        return stock;
    }
    
    public List<ComponentQuantity> getComponents() {
        return new ArrayList<>(components);
    }
    
    @Override
    public boolean useStock(int quantity) {
        if (stock >= quantity) {
            stock -= quantity;
            return true;
        }
        return false;
    }
    
    /**
     * Ürünün içerdiği tüm bileşenlerin stoklarını kontrol eder
     * @return Tüm bileşenlerin stoğu yeterliyse true, değilse false
     */
    public boolean checkComponentsStock() {
        for (ComponentQuantity cq : components) {
            Component c = cq.getComponent();
            if (c.getStock() < cq.getQuantity()) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Ürünün tüm bileşenlerinden belirtilen miktarda kullanır
     * @return İşlem başarılı ise true, değilse false
     */
    public boolean useComponentsStock() {
        // Önce stokların yeterli olduğunu kontrol et
        if (!checkComponentsStock()) {
            return false;
        }
        
        // Bileşenlerin stoklarını azalt
        for (ComponentQuantity cq : components) {
            Component c = cq.getComponent();
            if (!c.useStock(cq.getQuantity())) {
                return false; // Bu noktada gerçekleşmemeli çünkü zaten kontrol ettik
            }
        }
        
        // Ürünün kendi stoğunu bir artır (üretim sonrası)
        stock++;
        return true;
    }
    
    @Override
    public String generateReport() {
        StringBuilder report = new StringBuilder();
        report.append(String.format("Ürün: %s (ID: %s) - Toplam Maliyet: %.2f, Toplam Ağırlık: %.2f, Stok: %d\n", 
                                    name, id, getCost(), getWeight(), stock));
        report.append("Bileşenler:\n");
        
        for (ComponentQuantity cq : components) {
            report.append("  - ").append(cq.toString()).append("\n");
        }
        
        return report.toString();
    }
    
    @Override
    public String toString() {
        return name;
    }
    
    /**
     * Bir bileşeni belirtilen miktarda ürüne ekler.
     * Bu metot, add(Component, int) metoduyla aynı işlevi görür.
     * 
     * @param component Eklenecek bileşen
     * @param quantity Miktar
     */
    public void addComponent(Component component, int quantity) {
        add(component, quantity);
    }
} 