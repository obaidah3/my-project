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
    private final String oilPressureCondition;
    private final String coolingCondition;
    private final String transmissionCondition;
    private final String emissionCondition;
    private final String drivingBehaviorCondition;
    private final String overallSummary;
    private final String faultDetails;
    private final int healthScore;
    private final double oilPressure;
    private final double coolantLevel;
    private final double transmissionTemperature;
    private final double throttlePosition;
    private final double mafReading;
    private final double oxygenSensorVoltage;
    private final int mileage;

    /**
     * Creates a new DiagnosticReport with extended sensor data and health score.
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
                           String oilPressureCondition,
                           String coolingCondition,
                           String transmissionCondition,
                           String emissionCondition,
                           String drivingBehaviorCondition,
                           String overallSummary,
                           String faultDetails,
                           int healthScore,
                           double oilPressure,
                           double coolantLevel,
                           double transmissionTemperature,
                           double throttlePosition,
                           double mafReading,
                           double oxygenSensorVoltage,
                           int mileage) {
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
        this.oilPressureCondition = oilPressureCondition;
        this.coolingCondition = coolingCondition;
        this.transmissionCondition = transmissionCondition;
        this.emissionCondition = emissionCondition;
        this.drivingBehaviorCondition = drivingBehaviorCondition;
        this.overallSummary = overallSummary;
        this.faultDetails = faultDetails;
        this.healthScore = healthScore;
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
     * Creates a new DiagnosticReport with analysis results and health score (backward compat).
     * Extended sensor fields default to safe values.
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
                           String faultDetails,
                           int healthScore) {
        this(vehicle, speed, rpm, engineTemperature, batteryVoltage, fuelLevel, faultCode,
             engineCondition, batteryCondition, fuelCondition,
             "Oil Pressure Normal", "Cooling System Healthy", "Transmission Healthy",
             "Emission Sensors Normal", "Normal Driving Behavior",
             overallSummary, faultDetails, healthScore,
             0.0, 100.0, 80.0, 0.0, 0.0, 0.45, 0);
    }

    /**
     * Creates a new DiagnosticReport with a default health score of 100.
     * This overload preserves compatibility for callers that do not provide a score.
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
        this(vehicle, speed, rpm, engineTemperature, batteryVoltage, fuelLevel, faultCode,
             engineCondition, batteryCondition, fuelCondition, overallSummary, faultDetails, 100);
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
        if (oilPressureCondition == null || oilPressureCondition.trim().isEmpty()) {
            throw new IllegalArgumentException("Oil pressure condition cannot be null or empty");
        }
        if (coolingCondition == null || coolingCondition.trim().isEmpty()) {
            throw new IllegalArgumentException("Cooling condition cannot be null or empty");
        }
        if (transmissionCondition == null || transmissionCondition.trim().isEmpty()) {
            throw new IllegalArgumentException("Transmission condition cannot be null or empty");
        }
        if (emissionCondition == null || emissionCondition.trim().isEmpty()) {
            throw new IllegalArgumentException("Emission condition cannot be null or empty");
        }
        if (drivingBehaviorCondition == null || drivingBehaviorCondition.trim().isEmpty()) {
            throw new IllegalArgumentException("Driving behavior condition cannot be null or empty");
        }
        if (overallSummary == null || overallSummary.trim().isEmpty()) {
            throw new IllegalArgumentException("Overall summary cannot be null or empty");
        }
        if (faultDetails == null) {
            throw new IllegalArgumentException("Fault details cannot be null");
        }
        if (healthScore < 0 || healthScore > 100) {
            throw new IllegalArgumentException("Health score must be between 0 and 100");
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

    public int getHealthScore() {
        return healthScore;
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

    public String getOilPressureCondition() {
        return oilPressureCondition;
    }

    public String getCoolingCondition() {
        return coolingCondition;
    }

    public String getTransmissionCondition() {
        return transmissionCondition;
    }

    public String getEmissionCondition() {
        return emissionCondition;
    }

    public String getDrivingBehaviorCondition() {
        return drivingBehaviorCondition;
    }

    public String getOverallSummary() {
        return overallSummary;
    }

    public String getFaultDetails() {
        return faultDetails;
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
        sb.append("Oil Pressure: ").append(String.format("%.1f", oilPressure)).append(" bar\n");
        sb.append("Coolant Level: ").append(String.format("%.1f", coolantLevel)).append(" %\n");
        sb.append("Transmission Temperature: ").append(String.format("%.1f", transmissionTemperature)).append(" °C\n");
        sb.append("Throttle Position: ").append(String.format("%.1f", throttlePosition)).append(" %\n");
        sb.append("MAF Reading: ").append(String.format("%.1f", mafReading)).append(" g/s\n");
        sb.append("Oxygen Sensor Voltage: ").append(String.format("%.2f", oxygenSensorVoltage)).append(" V\n");
        sb.append("Mileage: ").append(mileage).append(" km\n");
        sb.append("\n--- Analysis Results ---\n");
        sb.append("Engine Condition: ").append(engineCondition).append("\n");
        sb.append("Battery Condition: ").append(batteryCondition).append("\n");
        sb.append("Fuel Condition: ").append(fuelCondition).append("\n");
        sb.append("Oil Pressure Condition: ").append(oilPressureCondition).append("\n");
        sb.append("Cooling System Condition: ").append(coolingCondition).append("\n");
        sb.append("Transmission Condition: ").append(transmissionCondition).append("\n");
        sb.append("Emission Condition: ").append(emissionCondition).append("\n");
        sb.append("Driving Behavior: ").append(drivingBehaviorCondition).append("\n");
        sb.append("\nOverall Summary: ").append(overallSummary).append("\n");
        sb.append("Health Score: ").append(healthScore).append("/100\n");
        if (!faultDetails.isEmpty()) {
            sb.append("\nFault Details: ").append(faultDetails).append("\n");
        }
        return sb.toString();
    }
}