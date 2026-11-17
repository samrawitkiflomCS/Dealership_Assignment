package org.example;

public class Main {
    public static void main(String[] args) {
        Dealership dealership = new Dealership();
        dealership.loadFromFile("vehicles.txt");
        dealership.processAndPrint("outputDealership.txt");
    }
}