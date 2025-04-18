package com.manufacturing.system.presentation;

import com.manufacturing.system.application.ManufacturingService;

public class ConsoleUI {
    private final Logger logger;
    private final ManufacturingService manufacturingService;

    public ConsoleUI() {
        this.logger = new Logger(false);
        this.manufacturingService = new ManufacturingService(logger);
    }

    public void startManufacturingProcess() {
        logger.log("\n========== PRODUCTION COMPANY SYSTEM ==========\n");
        String report = manufacturingService.startManufacturing();
        logger.log(report);
        logger.log("\n========== PROGRAM ENDED ==========");
    }
} 