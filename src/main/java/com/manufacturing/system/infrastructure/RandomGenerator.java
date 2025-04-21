package com.manufacturing.system.infrastructure;

import java.util.Random;

public class RandomGenerator {
    private static final Random random = new Random();
    
    private RandomGenerator() { }
    
    /**
     * Sets the seed of the random number generator
     * This is useful for testing to ensure consistent results
     * @param seed the seed to set
     */
    public static void setSeed(long seed) {
        random.setSeed(seed);
    }
    
    /**
     * Generates random numbers in the specified range
     * @param min Minimum value
     * @param max Maximum value
     * @return Generated number
     */
    public static int generateNumber(int min, int max) {
        return min + random.nextInt(max - min + 1);
    }
    
    /**
     * Randomly determines the production outcome
     * @return 1: Successful, 2: System Error, 3: Damaged Component
     */
    public static int generateManufacturingOutcome() {
        return generateNumber(1, 3);
    }
    
    /**
     * Determines if there will be a stock shortage
     * @return true if there will be a stock shortage, false otherwise
     */
    public static boolean shouldSimulateStockShortage() {
        // 15% chance of stock shortage
        return random.nextDouble() < 0.15;
    }
    
    /**
     * Determines success with a certain probability
     * @param successProbability probability of success (0.0 - 1.0)
     * @return if success true, otherwise false
     */
    public static boolean isSuccessful(double successProbability) {
        return random.nextDouble() < successProbability;
    }
} 