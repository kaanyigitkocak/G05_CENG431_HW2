package com.manufacturing.system.domain.state;

import com.manufacturing.system.domain.model.Product;

public class ManufacturingProcess {
    private ManufacturingState currentState;
    private final Product product;
    private final int quantity;
    private String failureReason;

    private int successCount = 0;
    private int systemErrorCount = 0;
    private int damagedComponentCount = 0;
    private int stockShortageCount = 0;
    
    public ManufacturingProcess(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        this.currentState = new WaitingForStockState();
    }

    public void run() {
        for (int i = 0; i < quantity; i++) {
            System.out.println("\n" + product.getName() + " production starts (" + (i + 1) + "/" + quantity + ")...");
            this.currentState = new WaitingForStockState();
            this.currentState.handle(this);
        }
    }

    public void setState(ManufacturingState state) {
        this.currentState = state;
        System.out.println("The situation has changed: " + state.getName());
    }

    public ManufacturingState getCurrentState() {
        return currentState;
    }

    public Product getProduct() {
        return product;
    }

    public void incrementSuccessCount() {
        successCount++;
    }

    public void incrementSystemErrorCount() {
        systemErrorCount++;
    }

    public void incrementDamagedComponentCount() {
        damagedComponentCount++;
    }

    public void incrementStockShortageCount() {
        stockShortageCount++;
    }

    public void setFailureReason(String reason) {
        this.failureReason = reason;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public String generateReport() {
        StringBuilder report = new StringBuilder();
        report.append("\n==== ").append(product.getName()).append(" Production Report ====\n");
        report.append("Production Request: ").append(quantity).append(" quantity\n");
        report.append("Successful Production: ").append(successCount).append(" quantity\n");
        
        if (successCount > 0) {
            report.append("  - Unit Cost: ").append(String.format("%.2f", product.getCost())).append("\n");
            report.append("  - Unit Weight: ").append(String.format("%.2f", product.getWeight())).append("\n");
            report.append("  - Total Cost: ").append(String.format("%.2f", product.getCost() * successCount)).append("\n");
            report.append("  - Total Weight: ").append(String.format("%.2f", product.getWeight() * successCount)).append("\n");
        }
        
        report.append("Unsuccessful Production: ").append(systemErrorCount + damagedComponentCount + stockShortageCount).append(" quantity\n");
        
        if (systemErrorCount > 0) {
            report.append("  - System Error: ").append(systemErrorCount).append(" quantity\n");
        }
        
        if (damagedComponentCount > 0) {
            report.append("  - Damaged Component: ").append(damagedComponentCount).append(" quantity\n");
        }
        
        if (stockShortageCount > 0) {
            report.append("  - Stock Shortage: ").append(stockShortageCount).append(" quantity\n");
        }
        
        return report.toString();
    }

    public int getTotalProcessedCount() {
        return successCount + systemErrorCount + damagedComponentCount + stockShortageCount;
    }

    public int getSuccessCount() {
        return successCount;
    }

    public int getSystemErrorCount() {
        return systemErrorCount;
    }

    public int getDamagedComponentCount() {
        return damagedComponentCount;
    }

    public int getStockShortageCount() {
        return stockShortageCount;
    }
} 