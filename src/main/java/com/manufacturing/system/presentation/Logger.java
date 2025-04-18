package com.manufacturing.system.presentation;

public class Logger {
    private boolean debugMode;

    public Logger(boolean debugMode) {
        this.debugMode = debugMode;
    }

    public void log(String message) {
        System.out.println(message);
    }

    public void error(String message) {
        System.err.println("Error: " + message);
    }

    public void debug(String message) {
        if (debugMode) {
            System.out.println("DEBUG: " + message);
        }
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    public boolean isDebugMode() {
        return debugMode;
    }
} 