package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a base Vehicle with brand, model, pricing, and fuel type.
 */
public class Vehicle implements VehicleInterface, Comparable<Vehicle> {
    private static final Logger logger = LoggerFactory.getLogger(Vehicle.class);

    public enum FuelType {
        PETROL, DIESEL, HYBRID
    }

    protected String brand;
    protected String model;
    protected double basePrice;
    protected int age;
    protected double km;
    protected FuelType fuelType;

    /**
     * Constructs a Vehicle object.
     * @param brandString The full brand and model name.
     * @param basePrice The starting purchase price.
     * @param age The age of the vehicle in years.
     * @param km The odometer reading in kilometers.
     * @param fuelType The fuel type (PETROL, DIESEL, HYBRID).
     */
    public Vehicle(String brandString, double basePrice, int age, double km, FuelType fuelType) {
        logger.info("Creating Vehicle: " + brandString);
        String[] parts = brandString.split(" ", 2);
        this.brand = parts[0];
        this.model = parts.length > 1 ? parts[1] : "";
        this.basePrice = basePrice;
        this.age = age;
        this.km = km;
        this.fuelType = fuelType;
    }

    @Override
    public void applyDiscount(int percentage) {
        logger.info("Applying " + percentage + "% discount to " + brand + " " + model);
        this.basePrice -= this.basePrice * (percentage / 100.0);
    }

    @Override
    public int getFinalPrice() {
        logger.info("Calculating final price for " + brand + " " + model);
        double price = basePrice - (age * 150000);
        if (price < 200000) {
            price = 200000;
        }
        if (fuelType == FuelType.HYBRID) {
            price *= 1.15;
        } else if (fuelType == FuelType.DIESEL) {
            price *= 1.05;
        }
        return (int) price;
    }

    @Override
    public double averageKmPerYear() {
        logger.info("Calculating average km/year for " + brand + " " + model);
        if (age == 0) {
            return km;
        }
        return km / age;
    }

    @Override
    public int compareTo(Vehicle other) {
        return Integer.compare(this.getFinalPrice(), other.getFinalPrice());
    }

    @Override
    public String toString() {
        return String.format("%s %s | Price: %d | Avg km/year: %.2f | Age: %d | Fuel: %s",
                brand, model, getFinalPrice(), averageKmPerYear(), age, fuelType);
    }

    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public double getBasePrice() { return basePrice; }
    public int getAge() { return age; }
    public double getKm() { return km; }
    public FuelType getFuelType() { return fuelType; }
}