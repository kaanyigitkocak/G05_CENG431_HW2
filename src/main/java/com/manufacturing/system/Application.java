package com.manufacturing.system;

import com.manufacturing.system.presentation.ConsoleUI;


public class Application {
    public static void main(String[] args) {
        ConsoleUI ui = new ConsoleUI();
        ui.startManufacturingProcess();
    }
} 