package com.vehiclediag.model;

import com.vehiclediag.util.ValidationUtil;
import java.io.Serializable;

/**
 * Represents a diagnostic request sent from the client to the server.
 * Contains vehicle information and live sensor readings.
 */
public class DiagnosticRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Vehicle vehicle;
    private final double speed;
    private final int rpm;
    private final double engineTemperature;
    private final double batteryVoltage;
    private final double fuelLevel;
    private final String faultCode;

    /**
     * Creates a new DiagnosticRequest.
     *
     * @param vehicle the vehicle being diagnosed
     * @param speed current vehicle speed in km/h
     * @param rpm current engine RPM
     * @param engineTemperature current engine temperature in Celsius
     * @param batteryVoltage current battery voltage in volts
     * @param fuelLevel current fuel level percentage
     * @param faultCode reported fault code, may be null or empty
     */
    public DiagnosticRequest(Vehicle vehicle,
                             double speed,
                             int rpm,
                             double engineTemperature,
                             double batteryVoltage,
                             double fuelLevel,
                             String faultCode) {
        this.vehicle = vehicle;
        this.speed = speed;
        this.rpm = rpm;
        this.engineTemperature = engineTemperature;
        this.batteryVoltage = batteryVoltage;
        this.fuelLevel = fuelLevel;
        this.faultCode = faultCode;
        validate();
    }

    /**
     * Validates the diagnostic request fields.
     *
     * @throws IllegalArgumentException if any field is invalid
     */
    public void validate() {
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle cannot be null");
        }

        ValidationUtil.validateVehicleId(vehicle.getVehicleId());
        ValidationUtil.validateModel(vehicle.getModel());
        ValidationUtil.validateYear(vehicle.getYear());
        ValidationUtil.validateSpeed(speed);
        ValidationUtil.validateRPM(rpm);
        ValidationUtil.validateTemperature(engineTemperature);
        ValidationUtil.validateBatteryVoltage(batteryVoltage);
        ValidationUtil.validateFuelLevel(fuelLevel);
        ValidationUtil.validateFaultCode(faultCode);
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public double getSpeed() {
        return speed;
    }

    public int getRpm() {
        return rpm;
    }

    public double getEngineTemperature() {
        return engineTemperature;
    }

    public double getBatteryVoltage() {
        return batteryVoltage;
    }

    public double getFuelLevel() {
        return fuelLevel;
    }

    public String getFaultCode() {
        return faultCode;
    }
}
