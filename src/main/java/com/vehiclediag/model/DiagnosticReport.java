package com.vehiclediag.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents the result of a vehicle diagnostic analysis.
 * Contains the original diagnostic request data along with analysis results
 * from all diagnostic analyzers.
 */
public class DiagnosticReport implements Serializable {

    private static final long serialVersionUID = 1L;

    private final LocalDateTime timestamp;
    private final Vehicle vehicle;
    private final double speed;
    private final int rpm;
    private final double engineTemperature;
    private final double batteryVoltage;
    private final double fuelLevel;
    private final String faultCode;
    private final String engineCondition;
    private final String batteryCondition;
    private final String fuelCondition;
    private final String overallSummary;
    private final String faultDetails;

    /**
     * Creates a new DiagnosticReport with analysis results.
     *
     * @param vehicle the vehicle that was diagnosed
     * @param speed vehicle speed at time of diagnosis
     * @param rpm engine RPM at time of diagnosis
     * @param engineTemperature engine temperature at time of diagnosis
     * @param batteryVoltage battery voltage at time of diagnosis
     * @param fuelLevel fuel level at time of diagnosis
     * @param faultCode reported fault code, may be null or empty
     * @param engineCondition analysis result from engine analyzer
     * @param batteryCondition analysis result from battery analyzer
     * @param fuelCondition analysis result from fuel analyzer
     * @param overallSummary overall diagnostic summary
     * @param faultDetails details of any detected faults
     */
    public DiagnosticReport(Vehicle vehicle,
                           double speed,
                           int rpm,
                           double engineTemperature,
                           double batteryVoltage,
                           double fuelLevel,
                           String faultCode,
                           String engineCondition,
                           String batteryCondition,
                           String fuelCondition,
                           String overallSummary,
                           String faultDetails) {
        this.timestamp = LocalDateTime.now();
        this.vehicle = vehicle;
        this.speed = speed;
        this.rpm = rpm;
        this.engineTemperature = engineTemperature;
        this.batteryVoltage = batteryVoltage;
        this.fuelLevel = fuelLevel;
        this.faultCode = faultCode;
        this.engineCondition = engineCondition;
        this.batteryCondition = batteryCondition;
        this.fuelCondition = fuelCondition;
        this.overallSummary = overallSummary;
        this.faultDetails = faultDetails;
        validate();
    }

    /**
     * Validates the diagnostic report fields.
     *
     * @throws IllegalArgumentException if any field is invalid
     */
    public void validate() {
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle cannot be null");
        }
        if (engineCondition == null || engineCondition.trim().isEmpty()) {
            throw new IllegalArgumentException("Engine condition cannot be null or empty");
        }
        if (batteryCondition == null || batteryCondition.trim().isEmpty()) {
            throw new IllegalArgumentException("Battery condition cannot be null or empty");
        }
        if (fuelCondition == null || fuelCondition.trim().isEmpty()) {
            throw new IllegalArgumentException("Fuel condition cannot be null or empty");
        }
        if (overallSummary == null || overallSummary.trim().isEmpty()) {
            throw new IllegalArgumentException("Overall summary cannot be null or empty");
        }
        if (faultDetails == null) {
            throw new IllegalArgumentException("Fault details cannot be null");
        }
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
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

    public String getEngineCondition() {
        return engineCondition;
    }

    public String getBatteryCondition() {
        return batteryCondition;
    }

    public String getFuelCondition() {
        return fuelCondition;
    }

    public String getOverallSummary() {
        return overallSummary;
    }

    public String getFaultDetails() {
        return faultDetails;
    }

    /**
     * Returns a formatted string representation of the diagnostic report.
     *
     * @return formatted diagnostic report
     */
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        StringBuilder sb = new StringBuilder();
        sb.append("=== VEHICLE DIAGNOSTIC REPORT ===\n");
        sb.append("Timestamp: ").append(timestamp.format(formatter)).append("\n");
        sb.append("Vehicle: ").append(vehicle.getVehicleId()).append(" (")
          .append(vehicle.getVehicleType()).append(" ").append(vehicle.getModel())
          .append(" ").append(vehicle.getYear()).append(")\n");
        sb.append("\n--- Sensor Readings ---\n");
        sb.append("Speed: ").append(speed).append(" km/h\n");
        sb.append("RPM: ").append(rpm).append("\n");
        sb.append("Engine Temperature: ").append(engineTemperature).append(" °C\n");
        sb.append("Battery Voltage: ").append(batteryVoltage).append(" V\n");
        sb.append("Fuel Level: ").append(fuelLevel).append(" %\n");
        sb.append("Fault Code: ").append(faultCode != null ? faultCode : "None").append("\n");
        sb.append("\n--- Analysis Results ---\n");
        sb.append("Engine Condition: ").append(engineCondition).append("\n");
        sb.append("Battery Condition: ").append(batteryCondition).append("\n");
        sb.append("Fuel Condition: ").append(fuelCondition).append("\n");
        sb.append("\nOverall Summary: ").append(overallSummary).append("\n");
        if (!faultDetails.isEmpty()) {
            sb.append("\nFault Details: ").append(faultDetails).append("\n");
        }
        return sb.toString();
    }
}