package com.vehiclediag.model;

/**
 * Concrete vehicle implementation for trucks.
 */
public class Truck extends Vehicle {

    /**
     * Creates a new Truck instance.
     *
     * @param vehicleId unique identifier for the truck
     * @param model truck model name
     * @param year manufacture year
     */
    public Truck(String vehicleId, String model, int year) {
        super(vehicleId, "Truck", model, year);
    }
}
