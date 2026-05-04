package com.vehiclediag.util;

/**
 * System-wide constants for the Smart Vehicle Diagnostic & Fault Detection System.
 * All configuration values, thresholds, and limits are centralized here.
 *
 * @author Vehicle Diagnostic System
 * @version 1.0.0
 */
public final class Constants {

    // Server Configuration
    /** Default server port for diagnostic connections */
    public static final int SERVER_PORT = 5000;

    /** Maximum number of concurrent client connections */
    public static final int MAX_CLIENTS = 50;

    /** Response timeout in milliseconds for client operations */
    public static final int RESPONSE_TIMEOUT_MS = 3000;

    // CSV Configuration
    /** CSV file name for storing diagnostic reports */
    public static final String CSV_FILE = "DiagnosticReports.csv";

    /** CSV header row for diagnostic reports */
    public static final String[] CSV_HEADERS = {
        "timestamp", "vehicleId", "vehicleType", "model", "year",
        "speed", "rpm", "engineTemperature", "batteryVoltage", "fuelLevel", "faultCode",
        "engineCondition", "batteryCondition", "fuelCondition", "overallSummary"
    };

    // Vehicle Sensor Ranges
    /** Maximum vehicle speed in km/h */
    public static final int MAX_SPEED_KMH = 250;

    /** Maximum engine RPM */
    public static final int MAX_RPM = 8000;

    /** Minimum engine temperature in Celsius */
    public static final double MIN_TEMP_C = 50.0;

    /** Maximum engine temperature in Celsius */
    public static final double MAX_TEMP_C = 120.0;

    /** Minimum battery voltage in volts */
    public static final double MIN_BATTERY_V = 8.0;

    /** Maximum battery voltage in volts */
    public static final double MAX_BATTERY_V = 16.0;

    /** Minimum fuel level percentage */
    public static final double MIN_FUEL_LEVEL = 0.0;

    /** Maximum fuel level percentage */
    public static final double MAX_FUEL_LEVEL = 100.0;

    // Engine Analysis Thresholds
    /** Good condition RPM upper limit */
    public static final int ENGINE_GOOD_RPM_MAX = 6000;

    /** Moderate condition RPM upper limit */
    public static final int ENGINE_MODERATE_RPM_MAX = 7500;

    /** Critical condition RPM threshold */
    public static final int ENGINE_CRITICAL_RPM_MIN = 7500;

    /** Good condition temperature upper limit in Celsius */
    public static final double ENGINE_GOOD_TEMP_MAX = 100.0;

    /** Moderate condition temperature upper limit in Celsius */
    public static final double ENGINE_MODERATE_TEMP_MAX = 115.0;

    /** Critical condition temperature threshold in Celsius */
    public static final double ENGINE_CRITICAL_TEMP_MIN = 115.0;

    // Battery Analysis Thresholds
    /** Good condition battery voltage minimum in volts */
    public static final double BATTERY_GOOD_MIN_V = 13.0;

    /** Good condition battery voltage maximum in volts */
    public static final double BATTERY_GOOD_MAX_V = 14.5;

    /** Acceptable condition battery voltage minimum in volts */
    public static final double BATTERY_ACCEPTABLE_MIN_V = 12.0;

    /** Acceptable condition battery voltage maximum in volts */
    public static final double BATTERY_ACCEPTABLE_MAX_V = 13.0;

    /** Critical condition battery voltage threshold in volts */
    public static final double BATTERY_CRITICAL_MAX_V = 12.0;

    // Fuel Analysis Thresholds
    /** Fuel warning threshold percentage */
    public static final double FUEL_WARNING_THRESHOLD = 15.0;

    // Fault Code Analysis
    /** Common fault codes and their descriptions */
    public static final java.util.Map<String, String> FAULT_CODE_MAP = java.util.Map.ofEntries(
        java.util.Map.entry("P0001", "Fuel Volume Regulator Control Circuit/Open"),
        java.util.Map.entry("P0002", "Fuel Volume Regulator Control Circuit Range/Performance"),
        java.util.Map.entry("P0003", "Fuel Volume Regulator Control Circuit Low"),
        java.util.Map.entry("P0004", "Fuel Volume Regulator Control Circuit High"),
        java.util.Map.entry("P0100", "Mass or Volume Air Flow Circuit Malfunction"),
        java.util.Map.entry("P0101", "Mass or Volume Air Flow Circuit Range/Performance Problem"),
        java.util.Map.entry("P0102", "Mass or Volume Air Flow Circuit Low Input"),
        java.util.Map.entry("P0103", "Mass or Volume Air Flow Circuit High Input"),
        java.util.Map.entry("P0200", "Injector Circuit Malfunction"),
        java.util.Map.entry("P0300", "Random/Multiple Cylinder Misfire Detected"),
        java.util.Map.entry("P0301", "Cylinder 1 Misfire Detected"),
        java.util.Map.entry("P0302", "Cylinder 2 Misfire Detected"),
        java.util.Map.entry("P0303", "Cylinder 3 Misfire Detected"),
        java.util.Map.entry("P0304", "Cylinder 4 Misfire Detected"),
        java.util.Map.entry("P0400", "Exhaust Gas Recirculation Flow Malfunction"),
        java.util.Map.entry("P0401", "Exhaust Gas Recirculation Flow Insufficient Detected"),
        java.util.Map.entry("P0402", "Exhaust Gas Recirculation Flow Excessive Detected"),
        java.util.Map.entry("P0500", "Vehicle Speed Sensor Malfunction"),
        java.util.Map.entry("P0501", "Vehicle Speed Sensor Range/Performance"),
        java.util.Map.entry("P0502", "Vehicle Speed Sensor Low Input"),
        java.util.Map.entry("P0503", "Vehicle Speed Sensor Intermittent/Erratic/High"),
        java.util.Map.entry("P0600", "Serial Communication Link Malfunction")
    );

    // Logging Configuration
    /** Logger name for server components */
    public static final String LOGGER_SERVER = "com.vehiclediag.server";

    /** Logger name for client components */
    public static final String LOGGER_CLIENT = "com.vehiclediag.client";

    /** Logger name for analyzer components */
    public static final String LOGGER_ANALYZER = "com.vehiclediag.analyzer";

    /** Logger name for model components */
    public static final String LOGGER_MODEL = "com.vehiclediag.model";

    // Thread Pool Configuration
    /** Core pool size for server thread pool */
    public static final int THREAD_POOL_CORE_SIZE = 10;

    /** Maximum pool size for server thread pool */
    public static final int THREAD_POOL_MAX_SIZE = 50;

    /** Keep alive time for idle threads in seconds */
    public static final long THREAD_POOL_KEEP_ALIVE_SECONDS = 60;

    // Socket Configuration
    /** Socket timeout for client connections in milliseconds */
    public static final int SOCKET_TIMEOUT_MS = 120000;

    /** Socket buffer size for I/O operations */
    public static final int SOCKET_BUFFER_SIZE = 8192;

    // Utility class - prevent instantiation
    private Constants() {
        throw new UnsupportedOperationException("Constants class cannot be instantiated");
    }
}