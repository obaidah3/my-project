package com.vehiclediag.util;

import java.util.regex.Pattern;

/**
 * Utility class providing reusable validation methods for the Vehicle Diagnostic System.
 * All validation methods throw IllegalArgumentException with descriptive error messages
 * for invalid input values.
 *
 * @author Vehicle Diagnostic System
 * @version 1.0.0
 */
public final class ValidationUtil {

    // Regular expression patterns for validation
    private static final Pattern VEHICLE_ID_PATTERN = Pattern.compile("^[A-Z0-9]{3,20}$");
    private static final Pattern MODEL_PATTERN = Pattern.compile("^[A-Za-z0-9\\s\\-_]{1,50}$");
    private static final Pattern FAULT_CODE_PATTERN = Pattern.compile("^[A-Z0-9]{0,10}$");

    /**
     * Validates a vehicle ID string.
     * Vehicle ID must be 3-20 characters, containing only uppercase letters and digits.
     *
     * @param vehicleId the vehicle ID to validate
     * @throws IllegalArgumentException if vehicleId is null, empty, or invalid format
     */
    public static void validateVehicleId(String vehicleId) {
        if (vehicleId == null || vehicleId.trim().isEmpty()) {
            throw new IllegalArgumentException("Vehicle ID cannot be null or empty");
        }

        String trimmedId = vehicleId.trim();
        if (!VEHICLE_ID_PATTERN.matcher(trimmedId).matches()) {
            throw new IllegalArgumentException(
                "Vehicle ID must be 3-20 characters, containing only uppercase letters and digits. Got: '" + vehicleId + "'");
        }
    }

    /**
     * Validates a vehicle model string.
     * Model must be 1-50 characters, containing letters, digits, spaces, hyphens, and underscores.
     *
     * @param model the vehicle model to validate
     * @throws IllegalArgumentException if model is null, empty, or invalid format
     */
    public static void validateModel(String model) {
        if (model == null || model.trim().isEmpty()) {
            throw new IllegalArgumentException("Vehicle model cannot be null or empty");
        }

        String trimmedModel = model.trim();
        if (!MODEL_PATTERN.matcher(trimmedModel).matches()) {
            throw new IllegalArgumentException(
                "Vehicle model must be 1-50 characters, containing only letters, digits, spaces, hyphens, and underscores. Got: '" + model + "'");
        }
    }

    /**
     * Validates a vehicle year.
     * Year must be between 1900 and current year + 1.
     *
     * @param year the vehicle year to validate
     * @throws IllegalArgumentException if year is outside valid range
     */
    public static void validateYear(int year) {
        int currentYear = java.time.Year.now().getValue();
        if (year < 1900 || year > currentYear + 1) {
            throw new IllegalArgumentException(
                "Vehicle year must be between 1900 and " + (currentYear + 1) + ". Got: " + year);
        }
    }

    /**
     * Validates vehicle speed in km/h.
     * Speed must be between 0 and MAX_SPEED_KMH.
     *
     * @param speed the speed to validate
     * @throws IllegalArgumentException if speed is outside valid range
     */
    public static void validateSpeed(double speed) {
        if (speed < 0 || speed > Constants.MAX_SPEED_KMH) {
            throw new IllegalArgumentException(
                "Speed must be between 0 and " + Constants.MAX_SPEED_KMH + " km/h. Got: " + speed);
        }
    }

    /**
     * Validates engine RPM.
     * RPM must be between 0 and MAX_RPM.
     *
     * @param rpm the RPM value to validate
     * @throws IllegalArgumentException if RPM is outside valid range
     */
    public static void validateRPM(int rpm) {
        if (rpm < 0 || rpm > Constants.MAX_RPM) {
            throw new IllegalArgumentException(
                "RPM must be between 0 and " + Constants.MAX_RPM + ". Got: " + rpm);
        }
    }

    /**
     * Validates engine temperature in Celsius.
     * Temperature must be between MIN_TEMP_C and MAX_TEMP_C.
     *
     * @param temperature the temperature to validate
     * @throws IllegalArgumentException if temperature is outside valid range
     */
    public static void validateTemperature(double temperature) {
        if (temperature < Constants.MIN_TEMP_C || temperature > Constants.MAX_TEMP_C) {
            throw new IllegalArgumentException(
                "Engine temperature must be between " + Constants.MIN_TEMP_C + "°C and " +
                Constants.MAX_TEMP_C + "°C. Got: " + temperature);
        }
    }

    /**
     * Validates battery voltage in volts.
     * Voltage must be between MIN_BATTERY_V and MAX_BATTERY_V.
     *
     * @param voltage the battery voltage to validate
     * @throws IllegalArgumentException if voltage is outside valid range
     */
    public static void validateBatteryVoltage(double voltage) {
        if (voltage < Constants.MIN_BATTERY_V || voltage > Constants.MAX_BATTERY_V) {
            throw new IllegalArgumentException(
                "Battery voltage must be between " + Constants.MIN_BATTERY_V + "V and " +
                Constants.MAX_BATTERY_V + "V. Got: " + voltage);
        }
    }

    /**
     * Validates fuel level percentage.
     * Fuel level must be between MIN_FUEL_LEVEL and MAX_FUEL_LEVEL.
     *
     * @param fuelLevel the fuel level percentage to validate
     * @throws IllegalArgumentException if fuel level is outside valid range
     */
    public static void validateFuelLevel(double fuelLevel) {
        if (fuelLevel < Constants.MIN_FUEL_LEVEL || fuelLevel > Constants.MAX_FUEL_LEVEL) {
            throw new IllegalArgumentException(
                "Fuel level must be between " + Constants.MIN_FUEL_LEVEL + "% and " +
                Constants.MAX_FUEL_LEVEL + "%. Got: " + fuelLevel);
        }
    }

    /**
     * Validates a fault code string.
     * Fault code can be empty or up to 10 characters containing uppercase letters and digits.
     *
     * @param faultCode the fault code to validate (can be null or empty)
     * @throws IllegalArgumentException if fault code format is invalid
     */
    public static void validateFaultCode(String faultCode) {
        if (faultCode == null) {
            return; // null is acceptable
        }

        if (!FAULT_CODE_PATTERN.matcher(faultCode).matches()) {
            throw new IllegalArgumentException(
                "Fault code must be up to 10 characters, containing only uppercase letters and digits. Got: '" + faultCode + "'");
        }
    }

    /**
     * Validates that a vehicle type enum value is not null.
     * Note: This method will be updated when VehicleType enum is created.
     *
     * @param vehicleType the vehicle type to validate
     * @throws IllegalArgumentException if vehicleType is null
     */
    public static void validateVehicleType(Object vehicleType) {
        if (vehicleType == null) {
            throw new IllegalArgumentException("Vehicle type cannot be null");
        }
        // TODO: Add specific enum validation when VehicleType is implemented
    }

    /**
     * Validates a port number for server binding.
     * Port must be between 1024 and 65535 (avoiding system ports).
     *
     * @param port the port number to validate
     * @throws IllegalArgumentException if port is outside valid range
     */
    public static void validatePort(int port) {
        if (port < 1024 || port > 65535) {
            throw new IllegalArgumentException(
                "Port number must be between 1024 and 65535. Got: " + port);
        }
    }

    /**
     * Validates a hostname string for client connections.
     * Hostname can be "localhost", IP address, or valid domain name.
     *
     * @param hostname the hostname to validate
     * @throws IllegalArgumentException if hostname is null, empty, or invalid format
     */
    public static void validateHostname(String hostname) {
        if (hostname == null || hostname.trim().isEmpty()) {
            throw new IllegalArgumentException("Hostname cannot be null or empty");
        }

        String trimmedHostname = hostname.trim();

        // Check for localhost
        if ("localhost".equalsIgnoreCase(trimmedHostname)) {
            return;
        }

        // Check for valid IP address pattern
        Pattern ipPattern = Pattern.compile(
            "^((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)\\.?\\b){4}$");
        if (ipPattern.matcher(trimmedHostname).matches()) {
            return;
        }

        // Check for valid hostname pattern (simplified)
        Pattern hostnamePattern = Pattern.compile(
            "^[a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?(\\.[a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?)*$");
        if (!hostnamePattern.matcher(trimmedHostname).matches()) {
            throw new IllegalArgumentException(
                "Invalid hostname format. Must be 'localhost', valid IP address, or domain name. Got: '" + hostname + "'");
        }
    }

    // Utility class - prevent instantiation
    private ValidationUtil() {
        throw new UnsupportedOperationException("ValidationUtil class cannot be instantiated");
    }
}