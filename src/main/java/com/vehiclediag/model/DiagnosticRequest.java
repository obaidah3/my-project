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
    private final double oilPressure;
    private final double coolantLevel;
    private final double transmissionTemperature;
    private final double throttlePosition;
    private final double mafReading;
    private final double oxygenSensorVoltage;
    private final int mileage;

    /**
     * Creates a new DiagnosticRequest with extended sensor data.
     *
     * @param vehicle the vehicle being diagnosed
     * @param speed current vehicle speed in km/h
     * @param rpm current engine RPM
     * @param engineTemperature current engine temperature in Celsius
     * @param batteryVoltage current battery voltage in volts
     * @param fuelLevel current fuel level percentage
     * @param faultCode reported fault code, may be null or empty
     * @param oilPressure current oil pressure in bar
     * @param coolantLevel current coolant level percentage
     * @param transmissionTemperature current transmission temperature in Celsius
     * @param throttlePosition current throttle position percentage
     * @param mafReading current mass air flow reading in g/s
     * @param oxygenSensorVoltage current oxygen sensor voltage
     * @param mileage vehicle mileage in kilometers
     */
    public DiagnosticRequest(Vehicle vehicle,
                             double speed,
                             int rpm,
                             double engineTemperature,
                             double batteryVoltage,
                             double fuelLevel,
                             String faultCode,
                             double oilPressure,
                             double coolantLevel,
                             double transmissionTemperature,
                             double throttlePosition,
                             double mafReading,
                             double oxygenSensorVoltage,
                             int mileage) {
        this.vehicle = vehicle;
        this.speed = speed;
        this.rpm = rpm;
        this.engineTemperature = engineTemperature;
        this.batteryVoltage = batteryVoltage;
        this.fuelLevel = fuelLevel;
        this.faultCode = faultCode;
        this.oilPressure = oilPressure;
        this.coolantLevel = coolantLevel;
        this.transmissionTemperature = transmissionTemperature;
        this.throttlePosition = throttlePosition;
        this.mafReading = mafReading;
        this.oxygenSensorVoltage = oxygenSensorVoltage;
        this.mileage = mileage;
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
        ValidationUtil.validateOilPressure(oilPressure);
        ValidationUtil.validateCoolantLevel(coolantLevel);
        ValidationUtil.validateTransmissionTemperature(transmissionTemperature);
        ValidationUtil.validateThrottlePosition(throttlePosition);
        ValidationUtil.validateMafReading(mafReading);
        ValidationUtil.validateOxygenSensorVoltage(oxygenSensorVoltage);
        ValidationUtil.validateMileage(mileage);
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

    public double getOilPressure() {
        return oilPressure;
    }

    public double getCoolantLevel() {
        return coolantLevel;
    }

    public double getTransmissionTemperature() {
        return transmissionTemperature;
    }

    public double getThrottlePosition() {
        return throttlePosition;
    }

    public double getMafReading() {
        return mafReading;
    }

    public double getOxygenSensorVoltage() {
        return oxygenSensorVoltage;
    }

    public int getMileage() {
        return mileage;
    }
}
