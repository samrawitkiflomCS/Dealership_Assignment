package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

/**
 * Manages the inventory and operations of a vehicle dealership.
 * Handles loading vehicles from a file, processing analytics, and generating reports.
 */
public class Dealership {
    private static final Logger logger = LoggerFactory.getLogger(Dealership.class);
    private TreeSet<Vehicle> vehicles;

    /**
     * Constructs a new Dealership with an empty inventory.
     */
    public Dealership() {
        logger.info("Initializing Dealership.");
        vehicles = new TreeSet<>();
    }

    /**
     * Loads vehicles from a specified text file.
     * If the file cannot be read, it falls back to hardcoded data.
     *
     * @param filename The name of the file to read (e.g., "vehicles.txt").
     */
    public void loadFromFile(String filename) {
        logger.info("Attempting to load vehicles from file: " + filename);
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("#");
                if (parts[0].equals("VEHICLE")) {
                    Vehicle v = new Vehicle(
                            parts[1],
                            Double.parseDouble(parts[2]),
                            Integer.parseInt(parts[3]),
                            Double.parseDouble(parts[4]),
                            Vehicle.FuelType.valueOf(parts[5])
                    );
                    vehicles.add(v);
                } else if (parts[0].equals("ELECTRIC")) {
                    ElectricVehicle ev = new ElectricVehicle(
                            parts[1],
                            Double.parseDouble(parts[2]),
                            Integer.parseInt(parts[3]),
                            Double.parseDouble(parts[4]),
                            Vehicle.FuelType.valueOf(parts[5]),
                            Integer.parseInt(parts[6]),
                            parts[7].equalsIgnoreCase("yes")
                    );
                    vehicles.add(ev);
                }
            }
            logger.info("Successfully loaded " + vehicles.size() + " vehicles.");
        } catch (IOException e) {
            logger.error("Failed to read file: " + filename, e);
            System.out.println("Could not read file. Falling back to hardcoded data.");
            loadHardcodedData();
        }
    }

    /**
     * Loads default hardcoded vehicle data into the inventory if file reading fails.
     */
    private void loadHardcodedData() {
        logger.info("Loading hardcoded fallback data.");
        vehicles.add(new Vehicle("Toyota Corolla", 5500000, 4, 75000, Vehicle.FuelType.HYBRID));
        vehicles.add(new ElectricVehicle("Tesla Model 3", 11500000, 2, 38000, Vehicle.FuelType.HYBRID, 75, true));
    }

    /**
     * Processes the vehicle data and prints a report to the console and a file.
     *
     * @param outputFilename The name of the file to write the report to.
     */
    public void processAndPrint(String outputFilename) {
        logger.info("Starting report generation to: " + outputFilename);
        try (PrintWriter out = new PrintWriter(new FileWriter(outputFilename))) {
            java.util.function.Consumer<String> print = (str) -> {
                System.out.println(str);
                out.println(str);
            };

            print.accept("--- Dealership Analytics ---");

            double totalKm = 0;
            for (Vehicle v : vehicles) totalKm += v.getKm();
            double avgKm = vehicles.isEmpty() ? 0 : totalKm / vehicles.size();
            print.accept(String.format("1. Fleet Average Mileage: %.2f km", avgKm));

            Vehicle cheapest = vehicles.first();
            print.accept("2. Cheapest Vehicle: " + cheapest.toString());

            ElectricVehicle topEV = null;
            for (Vehicle v : vehicles) {
                if (v instanceof ElectricVehicle) {
                    if (topEV == null || v.getFinalPrice() > topEV.getFinalPrice()) {
                        topEV = (ElectricVehicle) v;
                    }
                }
            }
            if (topEV != null) {
                print.accept(String.format("3. Top Electric Vehicle: Battery %d kWh, Avg km/year: %.2f",
                        topEV.getBatteryCapacity(), topEV.averageKmPerYear()));
            }

            long totalValue = 0;
            for (Vehicle v : vehicles) {
                totalValue += v.getFinalPrice();
            }
            print.accept("4. Total Fleet Inventory Value: " + totalValue);

            double avgPrice = (double) totalValue / vehicles.size();
            print.accept(String.format("5. Hybrid Vehicles (<= avg price of %.2f):", avgPrice));
            for (Vehicle v : vehicles) {
                if (v.getFuelType() == Vehicle.FuelType.HYBRID && v.getFinalPrice() <= avgPrice) {
                    print.accept("   - " + v.toString());
                }
            }

            long youngCount = vehicles.stream().filter(v -> v.getAge() < 5).count();
            print.accept("6. Young Inventory Count (age < 5): " + youngCount);

            print.accept("7. Discount Simulation:");
            if (!vehicles.isEmpty()) {
                Vehicle selected = vehicles.last();
                print.accept("   Before discount: " + selected.toString());
                selected.applyDiscount(10);
                print.accept("   After 10% discount: " + selected.toString());
            }

            logger.info("Report generated successfully.");

        } catch (IOException e) {
            logger.error("Failed to write report to file: " + outputFilename, e);
            System.out.println("Error writing to output file.");
        }
    }
}