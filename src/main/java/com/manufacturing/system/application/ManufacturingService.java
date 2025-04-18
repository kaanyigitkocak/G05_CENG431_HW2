package com.manufacturing.system.application;

import com.manufacturing.system.data.ComponentRepository;
import com.manufacturing.system.data.ProductRepository;
import com.manufacturing.system.domain.model.Product;
import com.manufacturing.system.domain.model.ProductionOrder;
import com.manufacturing.system.domain.state.ManufacturingProcess;
import com.manufacturing.system.presentation.Logger;

import java.util.ArrayList;
import java.util.List;

public class ManufacturingService {
    private final ComponentRepository componentRepository;
    private final ProductRepository productRepository;
    private final Logger logger;
    private final List<ManufacturingProcess> completedProcesses = new ArrayList<>();
    
    public ManufacturingService(Logger logger) {
        this.logger = logger;
        this.componentRepository = new ComponentRepository("src/main/resources/components.csv", logger);
        this.productRepository = new ProductRepository("src/main/resources/products.csv", componentRepository, logger);
    }

    public String startManufacturing() {
        logger.log("Starting the Manufacturing Company System...");
        
        logger.log("Loading components...");
        componentRepository.loadComponents();
        
        logger.log("Products are loading...");
        productRepository.loadProducts();
        
        createSampleProductionOrders();
        
        logger.log("\n=== Production Begins ===");
        
        List<ProductionOrder> orders = productRepository.getProductionOrders();
        for (ProductionOrder order : orders) {
            Product product = order.getProduct();
            int quantity = order.getQuantity();
            
            logger.log("\n--- " + product.getName() + " Production (" + quantity + " quantity) ---");
            
            ManufacturingProcess process = new ManufacturingProcess(product, quantity);
            process.run();
            
            completedProcesses.add(process);
            logger.log(process.generateReport());
        }
        
        logger.log("\n=== Production Completed ===");
        
        return generateSummaryReport();
    }

    private void createSampleProductionOrders() {
        List<Product> products = productRepository.getAll();
        
        logger.log("Creating production orders...");

        for (Product product : products) {
            ProductionOrder order = new ProductionOrder(product, 1);
            productRepository.addProductionOrder(order);
            logger.log("Order created: " + product.getName() + " - 1 quantity");
        }
        
        logger.log("Total " + productRepository.getProductionOrders().size() + " order created.");
    }

    public String generateSummaryReport() {
        int totalSuccess = 0;
        int totalSystemError = 0;
        int totalDamagedComponent = 0;
        int totalStockShortage = 0;
        double totalCost = 0;
        double totalWeight = 0;
        
        for (ManufacturingProcess process : completedProcesses) {
            totalSuccess += process.getSuccessCount();
            totalSystemError += process.getSystemErrorCount();
            totalDamagedComponent += process.getDamagedComponentCount();
            totalStockShortage += process.getStockShortageCount();
            
            if (process.getSuccessCount() > 0) {
                totalCost += process.getProduct().getCost() * process.getSuccessCount();
                totalWeight += process.getProduct().getWeight() * process.getSuccessCount();
            }
        }
        
        StringBuilder report = new StringBuilder();
        report.append("\n====== SUMMARY REPORT ======\n");
        report.append("Total Successful Production: ").append(totalSuccess).append(" quantity\n");
        report.append("  - Total Cost: ").append(String.format("%.2f", totalCost)).append(" TL\n");
        report.append("  - Total Weight: ").append(String.format("%.2f", totalWeight)).append(" kg\n");
        report.append("Total Unsuccessful Production: ").append(totalSystemError + totalDamagedComponent + totalStockShortage).append(" quantity\n");
        report.append("  - System Error: ").append(totalSystemError).append(" quantity\n");
        report.append("  - Damaged Component: ").append(totalDamagedComponent).append(" quantity\n");
        report.append("  - Insufficient Stock: ").append(totalStockShortage).append(" quantity\n");
        
        return report.toString();
    }
} 