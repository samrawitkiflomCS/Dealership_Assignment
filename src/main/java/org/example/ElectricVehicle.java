package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents an Electric Vehicle with battery capacity and heat pump options.
 */
public class ElectricVehicle extends Vehicle implements ElectricInterface {
    private static final Logger logger = LoggerFactory.getLogger(ElectricVehicle.class);

    private int batteryCapacity;
    private boolean hasHeatPump;

    /**
     * Constructs an ElectricVehicle object.
     * @param brandString The full brand and model name.
     * @param basePrice The starting purchase price.
     * @param age The age of the vehicle in years.
     * @param km The odometer reading in kilometers.
     * @param fuelType The fuel type.
     * @param batteryCapacity The battery capacity in kWh.
     * @param hasHeatPump True if it has a heat pump.
     */
    public ElectricVehicle(String brandString, double basePrice, int age, double km, FuelType fuelType, int batteryCapacity, boolean hasHeatPump) {
        super(brandString, basePrice, age, km, fuelType);
        logger.info("Creating ElectricVehicle: " + brandString);
        this.batteryCapacity = batteryCapacity;
        this.hasHeatPump = hasHeatPump;
    }

    @Override
    public int getFinalPrice() {
        logger.info("Calculating final price for ElectricVehicle: " + brand + " " + model);
        int baseCalculatedPrice = super.getFinalPrice();
        double finalPrice = baseCalculatedPrice;

        if (batteryCapacity >= 60) {
            finalPrice *= 1.10;
        }
        if (hasHeatPump) {
            finalPrice *= 1.05;
        }
        return (int) finalPrice;
    }

    @Override
    public boolean hasSameRangeCategory(Vehicle other) {
        logger.info("Comparing range category with another vehicle.");
        if (other instanceof ElectricVehicle) {
            ElectricVehicle otherElectric = (ElectricVehicle) other;
            return Math.abs(this.batteryCapacity - otherElectric.batteryCapacity) <= 5;
        }
        return false;
    }

    @Override
    public int batteryCostShare() {
        logger.info("Calculating battery cost share.");
        return batteryCapacity * 25000;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" | Battery: %d kWh | Heat Pump: %s",
                batteryCapacity, hasHeatPump ? "Yes" : "No");
    }

    public int getBatteryCapacity() { return batteryCapacity; }
}