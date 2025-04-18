package com.manufacturing.system.domain.state;

public interface ManufacturingState {
    /**
     * Performs the function of the state and transitions to the new state if necessary
     * @param context Production process
     */
    void handle(ManufacturingProcess context);
    
    /**
     * Returns the name of the state
     * @return name of the state
     */
    String getName();
} 