package com.vehiclediag.model;

import java.io.Serializable;

/**
 * Abstract base class representing a generic vehicle in the Smart Vehicle Diagnostic System.
 * Concrete vehicle types such as Car and Truck extend this class.
 */
public abstract class Vehicle implements Serializable {

    private static final long serialVersionUID = 1L;

    protected final String vehicleId;
    protected final String vehicleType;
    protected final String model;
    protected final int year;

    /**
     * Creates a new Vehicle instance.
     *
     * @param vehicleId unique identifier for the vehicle
     * @param vehicleType type of the vehicle (e.g. "Car" or "Truck")
     * @param model vehicle model name
     * @param year manufacture year
     */
    protected Vehicle(String vehicleId, String vehicleType, String model, int year) {
        this.vehicleId = vehicleId;
        this.vehicleType = vehicleType;
        this.model = model;
        this.year = year;
    }

    /**
     * Returns the vehicle identifier.
     *
     * @return vehicle ID
     */
    public String getVehicleId() {
        return vehicleId;
    }

    /**
     * Returns the vehicle type string.
     *
     * @return vehicle type
     */
    public String getVehicleType() {
        return vehicleType;
    }

    /**
     * Returns the vehicle model.
     *
     * @return model name
     */
    public String getModel() {
        return model;
    }

    /**
     * Returns the vehicle year.
     *
     * @return manufacture year
     */
    public int getYear() {
        return year;
    }
}