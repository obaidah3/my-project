package com.vehiclediag.model;

/**
 * Concrete vehicle implementation for cars.
 */
public class Car extends Vehicle {

    /**
     * Creates a new Car instance.
     *
     * @param vehicleId unique identifier for the car
     * @param model car model name
     * @param year manufacture year
     */
    public Car(String vehicleId, String model, int year) {
        super(vehicleId, "Car", model, year);
    }
}
