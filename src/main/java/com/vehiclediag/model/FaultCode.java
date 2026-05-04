package com.vehiclediag.model;

import java.io.Serializable;

/**
 * Enumeration of common vehicle fault codes with their descriptions.
 * Provides a type-safe way to represent diagnostic trouble codes (DTCs).
 */
public enum FaultCode implements Serializable {

    // Engine Faults
    P0171("P0171", "Fuel Trim System Lean Bank 1", "Engine", "Medium", "Verify vacuum leaks, fuel pressure, and oxygen sensor operation."),
    P0174("P0174", "Fuel Trim System Lean Bank 2", "Engine", "Medium", "Inspect intake system and fuel delivery for leaks or restrictions."),
    P0335("P0335", "Crankshaft Position Sensor A Circuit Malfunction", "Engine", "High", "Check sensor wiring, connector, and timing components."),
    P0340("P0340", "Camshaft Position Sensor Circuit Malfunction", "Engine", "High", "Inspect sensor, timing belt/chain, and wiring harness."),
    P0420("P0420", "Catalyst System Efficiency Below Threshold (Bank 1)", "Engine", "High", "Inspect catalytic converter, oxygen sensors, and exhaust leaks."),
    P0430("P0430", "Catalyst System Efficiency Below Threshold (Bank 2)", "Engine", "High", "Inspect catalytic converter, oxygen sensors, and exhaust leaks."),
    P0455("P0455", "Evaporative Emission System Leak Detected (Gross Leak)", "Fuel", "Medium", "Inspect gas cap, hoses, and EVAP system components."),
    P0456("P0456", "Evaporative Emission System Leak Detected (Small Leak)", "Fuel", "Medium", "Perform smoke test and inspect EVAP hoses and valves."),
    P0480("P0480", "Cooling Fan 1 Control Circuit Malfunction", "Cooling", "Medium", "Check fan motor, relay, and fan control circuitry."),
    P0481("P0481", "Cooling Fan 2 Control Circuit Malfunction", "Cooling", "Medium", "Verify fan wiring and control module operation."),

    // Cooling Faults
    P0115("P0115", "Engine Coolant Temperature Sensor Circuit Malfunction", "Cooling", "High", "Inspect coolant sensor, wiring, and connector for faults."),
    P0116("P0116", "Engine Coolant Temperature Sensor Circuit Range/Performance", "Cooling", "High", "Check sensor resistance, wiring, and engine temperature behavior."),
    P0117("P0117", "Engine Coolant Temperature Sensor Circuit Low Input", "Cooling", "High", "Verify sensor voltage and coolant level before replacement."),
    P0118("P0118", "Engine Coolant Temperature Sensor Circuit High Input", "Cooling", "High", "Inspect sensor, wiring, and ECU reference voltage."),
    P0128("P0128", "Coolant Thermostat Below Regulating Temperature", "Cooling", "Medium", "Confirm thermostat operation and coolant circulation."),
    P0217("P0217", "Engine Over Temperature Condition", "Cooling", "Critical", "Stop the vehicle and inspect cooling system immediately."),

    // Fuel Faults
    P0087("P0087", "Fuel Rail/System Pressure Too Low", "Fuel", "High", "Check fuel pump, pressure regulator, and fuel filters."),
    P0190("P0190", "Fuel Rail Pressure Sensor Circuit Malfunction", "Fuel", "Medium", "Inspect sensor wiring and rail pressure circuit."),
    P0193("P0193", "Fuel Rail Pressure Sensor Circuit High Input", "Fuel", "Medium", "Check sensor, wiring, and fuel pressure dynamics."),
    P0200("P0200", "Injector Circuit Malfunction", "Fuel", "High", "Verify injector wiring, connectors, and injector function."),
    P0201("P0201", "Injector Circuit/Open Cylinder 1", "Fuel", "High", "Inspect injector circuit and replace faulty injector if necessary."),
    P0202("P0202", "Injector Circuit/Open Cylinder 2", "Fuel", "High", "Inspect injector circuit and replace faulty injector if necessary."),
    P0203("P0203", "Injector Circuit/Open Cylinder 3", "Fuel", "High", "Inspect injector circuit and replace faulty injector if necessary."),
    P0204("P0204", "Injector Circuit/Open Cylinder 4", "Fuel", "High", "Inspect injector circuit and replace faulty injector if necessary."),
    P0205("P0205", "Injector Circuit/Open Cylinder 5", "Fuel", "High", "Inspect injector circuit and replace faulty injector if necessary."),
    P0206("P0206", "Injector Circuit/Open Cylinder 6", "Fuel", "High", "Inspect injector circuit and replace faulty injector if necessary."),

    // Ignition / Misfire Faults
    P0300("P0300", "Random/Multiple Cylinder Misfire Detected", "Ignition/Misfire", "High", "Inspect spark plugs, ignition coils, fuel delivery, and compression."),
    P0301("P0301", "Cylinder 1 Misfire Detected", "Ignition/Misfire", "High", "Check spark plug, coil, injector, and compression on cylinder 1."),
    P0302("P0302", "Cylinder 2 Misfire Detected", "Ignition/Misfire", "High", "Check spark plug, coil, injector, and compression on cylinder 2."),
    P0303("P0303", "Cylinder 3 Misfire Detected", "Ignition/Misfire", "High", "Check spark plug, coil, injector, and compression on cylinder 3."),
    P0304("P0304", "Cylinder 4 Misfire Detected", "Ignition/Misfire", "High", "Check spark plug, coil, injector, and compression on cylinder 4."),
    P0305("P0305", "Cylinder 5 Misfire Detected", "Ignition/Misfire", "High", "Check spark plug, coil, injector, and compression on cylinder 5."),
    P0306("P0306", "Cylinder 6 Misfire Detected", "Ignition/Misfire", "High", "Check spark plug, coil, injector, and compression on cylinder 6."),
    P0307("P0307", "Cylinder 7 Misfire Detected", "Ignition/Misfire", "High", "Check spark plug, coil, injector, and compression on cylinder 7."),
    P0308("P0308", "Cylinder 8 Misfire Detected", "Ignition/Misfire", "High", "Check spark plug, coil, injector, and compression on cylinder 8."),

    // Battery / Charging Faults
    P0562("P0562", "System Voltage Low", "Battery/Charging", "High", "Inspect battery condition, alternator output, and charging wiring."),
    P0622("P0622", "Generator Field Terminal Circuit Malfunction", "Battery/Charging", "Medium", "Verify alternator field circuit and voltage regulator."),
    P0623("P0623", "Generator Lamp/L-Terminal Circuit Malfunction", "Battery/Charging", "Medium", "Inspect alternator indicator lamp circuit and connector."),
    P0627("P0627", "Generator Lamp B Terminal Circuit Malfunction", "Battery/Charging", "Medium", "Inspect alternator wiring and battery connection."),
    P0628("P0628", "Generator Field Terminal Circuit/Open", "Battery/Charging", "Medium", "Check alternator field circuit wiring after verifying battery voltage."),

    // Transmission Faults
    P0700("P0700", "Transmission Control System Malfunction", "Transmission", "High", "Read transmission-specific codes and inspect control module."),
    P0705("P0705", "Transmission Range Sensor Circuit Malfunction", "Transmission", "High", "Inspect range sensor and wiring for correct operation."),
    P0715("P0715", "Input/Turbine Speed Sensor Circuit Malfunction", "Transmission", "High", "Verify speed sensor and transmission wiring harness."),
    P0720("P0720", "Output Speed Sensor Circuit Malfunction", "Transmission", "High", "Inspect output speed sensor and related wiring."),
    P0730("P0730", "Incorrect Gear Ratio", "Transmission", "High", "Check transmission fluid level, solenoids, and clutch operation."),
    P0735("P0735", "Incorrect Gear Ratio, 5th Gear", "Transmission", "High", "Inspect gear engagement components and control solenoids."),
    P0740("P0740", "Torque Converter Clutch Circuit Malfunction", "Transmission", "High", "Verify torque converter clutch solenoid and hydraulic circuit."),
    P0750("P0750", "Shift Solenoid A Malfunction", "Transmission", "High", "Inspect shift solenoid, valve body, and transmission wiring."),
    P0760("P0760", "Shift Solenoid C Malfunction", "Transmission", "High", "Inspect shift solenoid, hydraulic passages, and connectors."),
    P0775("P0775", "Pressure Control Solenoid Malfunction", "Transmission", "High", "Check solenoid operation and transmission hydraulic system."),

    // Sensor Faults
    P0100("P0100", "Mass or Volume Air Flow Circuit Malfunction", "Sensors", "Medium", "Inspect MAF sensor, wiring, and air intake for contamination."),
    P0101("P0101", "Mass or Volume Air Flow Circuit Range/Performance Problem", "Sensors", "Medium", "Clean or replace MAF sensor and verify sensor output."),
    P0102("P0102", "Mass or Volume Air Flow Circuit Low Input", "Sensors", "Medium", "Check sensor connection and intake leaks."),
    P0103("P0103", "Mass or Volume Air Flow Circuit High Input", "Sensors", "Medium", "Inspect MAF sensor and verify intake air conditions."),
    P0113("P0113", "Intake Air Temperature Sensor 1 Circuit High Input", "Sensors", "Medium", "Inspect IAT sensor and wiring for open or short conditions."),
    P0120("P0120", "Throttle Position Sensor A Circuit Malfunction", "Sensors", "Medium", "Verify throttle sensor wiring and connector integrity."),
    P0130("P0130", "Oxygen Sensor Circuit Malfunction (Bank 1 Sensor 1)", "Sensors", "Medium", "Inspect O2 sensor and exhaust system for leaks."),

    // Network / CAN Faults
    P0600("P0600", "Serial Communication Link Malfunction", "Network/CAN", "Medium", "Inspect data bus wiring, connectors, and control modules."),
    P0601("P0601", "Internal Control Module Memory Check Sum Error", "Network/CAN", "Medium", "Verify module power, ground, and update or replace module if needed."),
    P0602("P0602", "Control Module Programming Error", "Network/CAN", "Medium", "Confirm correct module software and perform reprogramming if required."),
    P0603("P0603", "Internal Control Module Keep Alive Memory Error", "Network/CAN", "Medium", "Inspect module battery backup and memory circuits."),
    P0604("P0604", "Internal Control Module RAM Error", "Network/CAN", "Medium", "Check module health and replace if memory faults persist."),
    P0605("P0605", "Internal Control Module ROM Error", "Network/CAN", "Medium", "Inspect module firmware integrity and reflash if available."),
    P0606("P0606", "PCM Processor Fault", "Network/CAN", "High", "Inspect ECU connections and consider module replacement."),
    P0607("P0607", "Control Module Device Fault", "Network/CAN", "Medium", "Verify module operation and communication with other modules."),
    P0608("P0608", "Control Module VVC/VVT System Fault", "Network/CAN", "Medium", "Inspect control module and variable valve timing components."),
    P0609("P0609", "Generator Control Circuit Fault", "Network/CAN", "Medium", "Inspect generator/alternator control wiring and ECU signals.");

    private static final long serialVersionUID = 1L;

    private final String code;
    private final String description;
    private final String systemCategory;
    private final String severity;
    private final String recommendation;

    /**
     * Creates a new FaultCode with detailed metadata.
     *
     * @param code the fault code string
     * @param description the human-readable description of the fault
     * @param systemCategory the system category for the code
     * @param severity the severity level for the code
     * @param recommendation the recommended diagnostic action
     */
    FaultCode(String code, String description, String systemCategory, String severity, String recommendation) {
        this.code = code;
        this.description = description;
        this.systemCategory = systemCategory;
        this.severity = severity;
        this.recommendation = recommendation;
    }

    /**
     * Returns the fault code string (e.g., "P0001").
     *
     * @return the code string
     */
    public String getCode() {
        return code;
    }

    /**
     * Returns the human-readable description of the fault code.
     *
     * @return the fault description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the system category this fault belongs to.
     *
     * @return the system category
     */
    public String getSystemCategory() {
        return systemCategory;
    }

    /**
     * Returns the severity level for this fault code.
     *
     * @return the severity level
     */
    public String getSeverity() {
        return severity;
    }

    /**
     * Returns the recommended action for diagnosing or repairing the fault.
     *
     * @return the recommendation
     */
    public String getRecommendation() {
        return recommendation;
    }

    /**
     * Finds a FaultCode by its code string.
     *
     * @param code the fault code string (e.g., "P0001")
     * @return the corresponding FaultCode, or null if not found
     */
    public static FaultCode fromCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            return null;
        }
        try {
            return FaultCode.valueOf(code.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Returns a formatted string representation of the fault code.
     *
     * @return formatted fault code with description and metadata
     */
    @Override
    public String toString() {
        return getCode() + ": " + description + " [" + systemCategory + ", " + severity + "]";
    }
}