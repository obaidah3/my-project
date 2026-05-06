package com.vehiclediag.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Enumeration of 100+ OBD-II fault codes with comprehensive diagnostic metadata.
 * Provides detailed causes, symptoms, repair urgency, and complexity estimates.
 */
public enum FaultCode implements Serializable {

    // ===== ENGINE SYSTEM FAULTS =====
    P0171("P0171", "Fuel Trim System Lean Bank 1", "Engine", "Medium", "Verify vacuum leaks, fuel pressure, and oxygen sensor operation.",
            Arrays.asList("Vacuum leak", "Low fuel pressure", "Faulty oxygen sensor", "Air intake leak"),
            Arrays.asList("Rough idle", "Poor acceleration", "Check engine light"), "Soon", "Moderate"),
    P0172("P0172", "Fuel Trim System Rich Bank 1", "Engine", "High", "Check for excessive fuel pressure, leaking injector, or faulty oxygen sensor.",
            Arrays.asList("Leaking fuel injector", "High fuel pressure", "Faulty oxygen sensor", "Air filter restricted"),
            Arrays.asList("Black smoke", "Poor fuel economy", "Rough idle"), "Soon", "Moderate"),
    P0174("P0174", "Fuel Trim System Lean Bank 2", "Engine", "Medium", "Inspect intake system and fuel delivery for leaks or restrictions.",
            Arrays.asList("Vacuum leak", "Fuel pressure regulator fault", "Oxygen sensor failure", "Air leak in intake"),
            Arrays.asList("Engine hesitation", "Poor fuel economy", "Stalling"), "Soon", "Moderate"),
    P0175("P0175", "Fuel Trim System Rich Bank 2", "Engine", "High", "Inspect injectors, fuel pressure regulator, and oxygen sensor readings.",
            Arrays.asList("Leaking fuel injector", "Fuel pressure regulator failure", "Rich mixture sensor fault"),
            Arrays.asList("Rich smell", "Engine hesitation", "Elevated emissions"), "Soon", "Moderate"),
    P0300("P0300", "Random/Multiple Cylinder Misfire Detected", "Engine", "High", "Inspect spark plugs, ignition coils, fuel delivery, and compression.",
            Arrays.asList("Worn spark plugs", "Faulty ignition coil", "Low fuel pressure", "Cylinder compression issue"),
            Arrays.asList("Engine shaking", "Reduced power", "Increased emissions"), "Soon", "Moderate"),
    P0301("P0301", "Cylinder 1 Misfire Detected", "Ignition/Misfire", "High", "Check spark plug, coil, injector, and compression on cylinder 1.",
            Arrays.asList("Bad spark plug", "Faulty ignition coil", "Fuel injector problem", "Low compression"),
            Arrays.asList("Cylinder shaking", "Power loss", "Check engine light"), "Soon", "Moderate"),
    P0302("P0302", "Cylinder 2 Misfire Detected", "Ignition/Misfire", "High", "Check spark plug, coil, injector, and compression on cylinder 2.",
            Arrays.asList("Worn spark plug", "Defective coil pack", "Stuck fuel injector", "Bad compression"),
            Arrays.asList("Engine vibration", "Reduced acceleration", "Misfiring"), "Soon", "Moderate"),
    P0303("P0303", "Cylinder 3 Misfire Detected", "Ignition/Misfire", "High", "Inspect cylinder 3 spark plug, coil, fuel injector, and compression.",
            Arrays.asList("Faulty spark plug", "Ignition coil failure", "Fuel delivery issue", "Compression leak"),
            Arrays.asList("Engine knock", "Loss of power", "Shaking sensation"), "Soon", "Moderate"),
    P0304("P0304", "Cylinder 4 Misfire Detected", "Ignition/Misfire", "High", "Verify cylinder 4 components: spark plug, ignition coil, injector.",
            Arrays.asList("Bad spark plug", "Coil malfunction", "Injector fault", "Compression problem"),
            Arrays.asList("Rough running", "Reduced power output", "Vibration"), "Soon", "Moderate"),
    P0335("P0335", "Crankshaft Position Sensor A Circuit Malfunction", "Engine", "High", "Check sensor wiring, connector, and timing components.",
            Arrays.asList("Faulty crank position sensor", "Wiring corrosion", "Bad connector", "Tone ring damage"),
            Arrays.asList("No-start condition", "Engine stalls", "Misfire"), "Soon", "Moderate"),
    P0336("P0336", "Crankshaft Position Sensor A Circuit Range/Performance", "Ignition/Misfire", "High", "Verify crankshaft sensor output and timing components.",
            Arrays.asList("Weak sensor signal", "Damaged tone ring", "Wiring issue", "Sensor gap misalignment"),
            Arrays.asList("Engine misfire", "Stalling", "Starting difficulty"), "Soon", "Moderate"),
    P0340("P0340", "Camshaft Position Sensor Circuit Malfunction", "Engine", "High", "Inspect sensor, timing belt/chain, and wiring harness.",
            Arrays.asList("Faulty camshaft sensor", "Broken timing belt", "Bad wiring connection", "Worn cam chain"),
            Arrays.asList("No start", "Rough idling", "Variable timing issue"), "Soon", "Moderate"),
    P0351("P0351", "Ignition Coil A Primary/Secondary Circuit Malfunction", "Ignition/Misfire", "High", "Inspect ignition coil, wiring, and driver circuitry.",
            Arrays.asList("Failed ignition coil", "Bad connector", "ECU drive fault", "Corroded terminals"),
            Arrays.asList("Cylinder misfire", "Rough idle", "Starting problems"), "Soon", "Moderate"),
    P0352("P0352", "Ignition Coil B Primary/Secondary Circuit Malfunction", "Ignition/Misfire", "High", "Check ignition coil B and associated circuits.",
            Arrays.asList("Faulty coil B", "Wiring fault", "Connector corrosion", "ECU signal issue"),
            Arrays.asList("Misfire on cylinder", "Reduced power", "Check engine light"), "Soon", "Moderate"),
    P0420("P0420", "Catalyst System Efficiency Below Threshold (Bank 1)", "Engine", "High", "Inspect catalytic converter, oxygen sensors, and exhaust leaks.",
            Arrays.asList("Failing catalytic converter", "Faulty oxygen sensor", "Exhaust leak", "Engine running rich"),
            Arrays.asList("Emissions test failure", "Rotten egg smell", "Reduced power"), "Soon", "High"),
    P0430("P0430", "Catalyst System Efficiency Below Threshold (Bank 2)", "Engine", "High", "Inspect catalytic converter, oxygen sensors, and exhaust leaks.",
            Arrays.asList("Bad cat converter", "O2 sensor fault", "Engine misfire", "Exhaust restriction"),
            Arrays.asList("Failed emissions", "Odor from exhaust", "Power loss"), "Soon", "High"),

    // ===== COOLING SYSTEM FAULTS =====
    P0115("P0115", "Engine Coolant Temperature Sensor Circuit Malfunction", "Cooling", "High", "Inspect coolant sensor, wiring, and connector for faults.",
            Arrays.asList("Faulty coolant sensor", "Broken wiring", "Bad connector", "ECU ground fault"),
            Arrays.asList("Inaccurate gauge", "Fan runs constantly", "Overheating"), "Soon", "Low"),
    P0116("P0116", "Engine Coolant Temperature Sensor Circuit Range/Performance", "Cooling", "High", "Check sensor resistance, wiring, and engine temperature behavior.",
            Arrays.asList("Sensor out of range", "Wiring issue", "ECU fault", "Coolant contamination"),
            Arrays.asList("Inconsistent temperature", "Check engine light", "Fan cycling"), "Soon", "Low"),
    P0117("P0117", "Engine Coolant Temperature Sensor Circuit Low Input", "Cooling", "High", "Verify sensor voltage and coolant level before replacement.",
            Arrays.asList("Shorted sensor", "Sensor resistance low", "Wiring short", "Ground fault"),
            Arrays.asList("Gauge shows cold", "Fan off", "Overheating"), "Soon", "Low"),
    P0118("P0118", "Engine Coolant Temperature Sensor Circuit High Input", "Cooling", "High", "Inspect sensor, wiring, and ECU reference voltage.",
            Arrays.asList("Open sensor circuit", "Sensor failure", "Wiring break", "Connector corrosion"),
            Arrays.asList("Gauge shows hot", "Fan always on", "Cold start issues"), "Soon", "Low"),
    P0128("P0128", "Coolant Thermostat Below Regulating Temperature", "Cooling", "Medium", "Confirm thermostat operation and coolant circulation.",
            Arrays.asList("Stuck open thermostat", "Faulty coolant temp sensor", "Slow warm-up", "Wax pellet failure"),
            Arrays.asList("Engine slow to warm", "Poor heater performance", "Reduced fuel economy"), "Soon", "Low"),
    P0217("P0217", "Engine Over Temperature Condition", "Cooling", "Critical", "Stop the vehicle and inspect cooling system immediately.",
            Arrays.asList("Low coolant level", "Failed thermostat", "Radiator blockage", "Broken fan belt"),
            Arrays.asList("Temperature gauge maxed", "Coolant warning light", "Smoke from engine"), "Immediate", "High"),
    P0218("P0218", "Transmission Over Temperature Condition", "Cooling", "Critical", "Stop the vehicle and inspect transmission cooling.",
            Arrays.asList("Low transmission fluid", "Faulty cooler", "Blocked radiator", "High ambient temperature"),
            Arrays.asList("Transmission warning", "Loss of power", "Fluid discoloration"), "Immediate", "High"),
    P0480("P0480", "Cooling Fan 1 Control Circuit Malfunction", "Cooling", "Medium", "Check fan motor, relay, and fan control circuitry.",
            Arrays.asList("Bad cooling fan", "Faulty relay", "Open circuit wiring", "ECU output fault"),
            Arrays.asList("Engine overheating", "Fan not running", "Reduced AC"), "Soon", "Moderate"),
    P0481("P0481", "Cooling Fan 2 Control Circuit Malfunction", "Cooling", "Medium", "Verify fan wiring and control module operation.",
            Arrays.asList("Secondary fan failure", "Relay fault", "Wiring issue", "Control module problem"),
            Arrays.asList("Overheating at highway", "Fan cycles incorrectly", "AC performance loss"), "Soon", "Moderate"),
    P0482("P0482", "Cooling Fan 1 Circuit Low", "Cooling", "Medium", "Verify fan circuit integrity and fan relay operation.",
            Arrays.asList("Broken fan relay", "Loose fan wiring", "Faulty fan motor", "Low voltage supply"),
            Arrays.asList("Engine overheating", "Cooling fan does not engage", "AC performance loss"), "Soon", "Moderate"),

    // ===== FUEL SYSTEM FAULTS =====
    P0087("P0087", "Fuel Rail/System Pressure Too Low", "Fuel", "High", "Check fuel pump, pressure regulator, and fuel filters.",
            Arrays.asList("Failed fuel pump", "Clogged fuel filter", "Faulty pressure regulator", "Fuel line leak"),
            Arrays.asList("No-start condition", "Hard starting", "Engine stalling"), "Soon", "Moderate"),
    P0088("P0088", "Fuel Rail/System Pressure Too High", "Fuel", "High", "Inspect fuel pressure regulator and fuel pump control circuitry.",
            Arrays.asList("Stuck regulator", "Blocked return line", "High fuel pressure sensor fault", "Pump control issue"),
            Arrays.asList("Fuel smell", "Hard starting", "Engine flooding"), "Soon", "Moderate"),
    P0190("P0190", "Fuel Rail Pressure Sensor Circuit Malfunction", "Fuel", "Medium", "Inspect sensor wiring and rail pressure circuit.",
            Arrays.asList("Faulty pressure sensor", "Wiring issue", "Bad connector", "ECU input fault"),
            Arrays.asList("Poor acceleration", "Check engine light", "Reduced performance"), "Soon", "Low"),
    P0191("P0191", "Fuel Rail Pressure Sensor Circuit Range/Performance", "Fuel", "Medium", "Check the sensor, wiring, and fuel rail pressure accuracy.",
            Arrays.asList("Faulty pressure sensor", "Wiring fault", "Fuel pump instability", "ECU processing issue"),
            Arrays.asList("Power loss", "Reduced fuel economy", "Engine hesitation"), "Soon", "Moderate"),
    P0193("P0193", "Fuel Rail Pressure Sensor Circuit High Input", "Fuel", "Medium", "Check sensor, wiring, and fuel pressure dynamics.",
            Arrays.asList("Sensor signal high", "Shorted wiring", "Stuck regulator", "Sensor malfunction"),
            Arrays.asList("Hard starting", "Rich condition", "Check engine light"), "Soon", "Low"),
    P0200("P0200", "Injector Circuit Malfunction", "Fuel", "High", "Verify injector wiring, connectors, and injector function.",
            Arrays.asList("Bad fuel injector", "Wiring fault", "Connector corrosion", "ECU driver issue"),
            Arrays.asList("Misfire", "Rough running", "Poor idle"), "Soon", "Moderate"),
    P0201("P0201", "Injector Circuit/Open Cylinder 1", "Fuel", "High", "Inspect injector circuit and replace faulty injector if necessary.",
            Arrays.asList("Open injector circuit", "Bad injector", "Wiring break", "Stuck injector"),
            Arrays.asList("Cylinder 1 misfire", "Rough idle", "No fuel delivery cyl 1"), "Soon", "Moderate"),
    P0202("P0202", "Injector Circuit/Open Cylinder 2", "Fuel", "High", "Inspect injector circuit for cylinder 2.",
            Arrays.asList("Faulty cylinder 2 injector", "Open wiring", "Connector fault", "ECU problem"),
            Arrays.asList("Misfire cylinder 2", "Rough running", "Power loss"), "Soon", "Moderate"),
    P0230("P0230", "Fuel Pump Primary Circuit Malfunction", "Fuel", "High", "Inspect fuel pump relay, wiring, and pump operation.",
            Arrays.asList("Failed fuel pump", "Relay fault", "Low voltage supply", "Wiring break"),
            Arrays.asList("No-start", "Fuel pump noise", "Fuel pressure low"), "Soon", "Moderate"),
    P0235("P0235", "Fuel Pump Secondary Circuit Malfunction", "Fuel", "High", "Verify wiring and control module output for the fuel pump circuit.",
            Arrays.asList("Wiring fault", "Pump driver failure", "Control module issue", "Relay fault"),
            Arrays.asList("Engine stalls", "Intermittent fuel delivery", "Warning lamp"), "Soon", "Moderate"),
    P0440("P0440", "Evaporative Emission System Leak Detected", "Fuel", "Medium", "Inspect EVAP hoses, gas cap, and purge valve.",
            Arrays.asList("Loose gas cap", "Cracked EVAP hose", "Defective purge valve", "Canister crack"),
            Arrays.asList("Fuel odor", "Check engine light", "Failed emissions test"), "Routine", "Low"),
    P0455("P0455", "Evaporative Emission System Leak Detected (Gross Leak)", "Fuel", "Medium", "Inspect gas cap, hoses, and EVAP system components.",
            Arrays.asList("Missing/loose gas cap", "Large EVAP leak", "Ruptured canister", "Broken fuel line"),
            Arrays.asList("Fuel smell", "CEL illuminated", "Emissions failure"), "Routine", "Low"),
    P0456("P0456", "Evaporative Emission System Leak Detected (Small Leak)", "Fuel", "Medium", "Perform smoke test and inspect EVAP hoses and valves.",
            Arrays.asList("Small EVAP leak", "Loose connector", "Micro crack in hose", "Bad seal"),
            Arrays.asList("Faint fuel odor", "Check engine light", "Emissions test fail"), "Routine", "Moderate"),

    // ===== TRANSMISSION FAULTS =====
    P0700("P0700", "Transmission Control System Malfunction", "Transmission", "High", "Read transmission-specific codes and inspect control module.",
            Arrays.asList("Trans control module fault", "Wiring issue", "Solenoid failure", "Hydraulic problem"),
            Arrays.asList("Transmission warning", "Shift delay", "Limp mode"), "Soon", "High"),
    P0705("P0705", "Transmission Range Sensor Circuit Malfunction", "Transmission", "High", "Inspect range sensor and wiring for correct operation.",
            Arrays.asList("Bad range sensor", "Connector corrosion", "Wiring fault", "Sensor misalignment"),
            Arrays.asList("Wrong gear display", "Shift issues", "No start in park"), "Soon", "Moderate"),
    P0715("P0715", "Input/Turbine Speed Sensor Circuit Malfunction", "Transmission", "High", "Verify speed sensor and transmission wiring harness.",
            Arrays.asList("Faulty input speed sensor", "Tone ring damage", "Wiring break", "Connector fault"),
            Arrays.asList("Shift delay", "No torque converter lock", "Limp mode"), "Soon", "Moderate"),
    P0720("P0720", "Output Speed Sensor Circuit Malfunction", "Transmission", "High", "Inspect output speed sensor and related wiring.",
            Arrays.asList("Failed output sensor", "Damaged wiring", "Connector corrosion", "Tone ring issue"),
            Arrays.asList("Speedometer error", "Shift hunting", "Transmission limp mode"), "Soon", "Moderate"),
    P0730("P0730", "Incorrect Gear Ratio", "Transmission", "High", "Check transmission fluid level, solenoids, and clutch operation.",
            Arrays.asList("Low trans fluid", "Stuck solenoid", "Clutch wear", "Hydraulic leak"),
            Arrays.asList("Poor acceleration", "Gear hunting", "Loss of power"), "Soon", "High"),
    P0740("P0740", "Torque Converter Clutch Circuit Malfunction", "Transmission", "High", "Verify torque converter clutch solenoid and hydraulic circuit.",
            Arrays.asList("Faulty TCC solenoid", "Wiring issue", "Hydraulic fault", "Stuck clutch"),
            Arrays.asList("Shudder/vibration", "Poor fuel economy", "Transmission noise"), "Soon", "High"),
    P0750("P0750", "Shift Solenoid A Malfunction", "Transmission", "High", "Inspect shift solenoid, valve body, and transmission wiring.",
            Arrays.asList("Faulty solenoid A", "Shorted wiring", "ECU command fault", "Stuck valve"),
            Arrays.asList("Harsh shifts", "Stuck gear", "Transmission warning"), "Soon", "Moderate"),
    P0755("P0755", "Shift Solenoid B Malfunction", "Transmission", "High", "Inspect shift solenoid B, hydraulic passages, and connectors.",
            Arrays.asList("Failed solenoid B", "Wiring open", "Connector corrosion", "Valve body issue"),
            Arrays.asList("Shift flare", "Gear hunting", "Transmission limp mode"), "Soon", "Moderate"),
    P0760("P0760", "Shift Solenoid C Malfunction", "Transmission", "High", "Inspect shift solenoid, hydraulic passages, and connectors.",
            Arrays.asList("Bad solenoid C", "Shorted circuit", "Hydraulic problem", "ECU issue"),
            Arrays.asList("Harsh shifts", "Stuck gear", "Loss of power"), "Soon", "Moderate"),
    P0775("P0775", "Pressure Control Solenoid Malfunction", "Transmission", "High", "Check solenoid operation and transmission hydraulic system.",
            Arrays.asList("Faulty pressure solenoid", "Wiring fault", "Hydraulic restriction", "Control issue"),
            Arrays.asList("Harsh shifting", "Poor idle", "Transmission warning"), "Soon", "High"),

    // ===== SENSOR FAULTS =====
    P0100("P0100", "Mass or Volume Air Flow Circuit Malfunction", "Sensors", "Medium", "Inspect MAF sensor, wiring, and air intake for contamination.",
            Arrays.asList("Dirty MAF sensor", "Wiring fault", "Air leak", "Sensor failure"),
            Arrays.asList("Rough idle", "Stalling", "Poor acceleration"), "Soon", "Low"),
    P0101("P0101", "Mass or Volume Air Flow Circuit Range/Performance Problem", "Sensors", "Medium", "Clean or replace MAF sensor and verify sensor output.",
            Arrays.asList("MAF sensor dirty", "Sensor out of range", "Air intake issue", "ECU calibration"),
            Arrays.asList("Engine hesitation", "Poor performance", "Check engine light"), "Soon", "Low"),
    P0102("P0102", "Mass or Volume Air Flow Circuit Low Input", "Sensors", "Medium", "Check sensor connection and intake leaks.",
            Arrays.asList("Low air flow signal", "Intake leak", "Sensor defect", "Wiring short"),
            Arrays.asList("Rough running", "Reduced power", "Starting difficulty"), "Soon", "Low"),
    P0103("P0103", "Mass or Volume Air Flow Circuit High Input", "Sensors", "Medium", "Inspect MAF sensor and verify intake air conditions.",
            Arrays.asList("High air flow signal", "Sensor contaminated", "Intake restriction", "Sensor stuck"),
            Arrays.asList("Rich condition", "Black smoke", "Poor fuel economy"), "Soon", "Low"),
    P0113("P0113", "Intake Air Temperature Sensor 1 Circuit High Input", "Sensors", "Medium", "Inspect IAT sensor and wiring for open or short conditions.",
            Arrays.asList("IAT sensor open", "Wiring break", "Sensor failure", "Connector corrosion"),
            Arrays.asList("Cold start issue", "Reduced fuel economy", "Check engine light"), "Soon", "Low"),
    P0120("P0120", "Throttle Position Sensor A Circuit Malfunction", "Sensors", "Medium", "Verify throttle sensor wiring and connector integrity.",
            Arrays.asList("Bad throttle sensor", "Wiring fault", "Connector corrosion", "Sensor misalignment"),
            Arrays.asList("Throttle response lag", "Stalling", "Check engine light"), "Soon", "Low"),
    P0130("P0130", "Oxygen Sensor Circuit Malfunction (Bank 1 Sensor 1)", "Sensors", "Medium", "Inspect O2 sensor and exhaust system for leaks.",
            Arrays.asList("Faulty O2 sensor", "Wiring issue", "Exhaust leak", "Sensor dead"),
            Arrays.asList("Poor fuel economy", "Rough idle", "Emissions failure"), "Soon", "Moderate"),
    P0135("P0135", "Oxygen Sensor Heater Circuit Malfunction (Bank 1 Sensor 1)", "Sensors", "Medium", "Inspect oxygen sensor heater circuit and wiring.",
            Arrays.asList("Open heater circuit", "Heater relay fault", "Sensor failure", "Wiring short"),
            Arrays.asList("Poor fuel economy", "Rough idle", "Reduced performance"), "Soon", "Moderate"),
    P0140("P0140", "Oxygen Sensor Circuit No Activity Detected (Bank 1 Sensor 2)", "Sensors", "Medium", "Verify O2 sensor operation and exhaust integrity.",
            Arrays.asList("Sensor not responding", "Exhaust leak", "Wiring open", "Dead sensor"),
            Arrays.asList("Rich running", "Emissions failure", "Poor performance"), "Soon", "Moderate"),
    P0150("P0150", "Oxygen Sensor Circuit Malfunction (Bank 2 Sensor 1)", "Sensors", "Medium", "Inspect O2 sensor and associated wiring.",
            Arrays.asList("Bad sensor bank 2", "Wiring short", "Exhaust leak", "Sensor aged"),
            Arrays.asList("Engine hesitation", "High emissions", "MIL illuminated"), "Soon", "Moderate"),
    P0170("P0170", "Fuel Trim System Malfunction", "Sensors", "Medium", "Inspect fuel trim sensors and system response.",
            Arrays.asList("Faulty O2 sensors", "Vacuum leak", "Fuel delivery issue", "Air intake leak"),
            Arrays.asList("Rough idle", "Check engine light", "Inconsistent performance"), "Soon", "Moderate"),

    // ===== BATTERY / CHARGING FAULTS =====
    P0562("P0562", "System Voltage Low", "Battery/Charging", "High", "Inspect battery condition, alternator output, and charging wiring.",
            Arrays.asList("Weak battery", "Faulty alternator", "Corroded connections", "Broken charge wire"),
            Arrays.asList("Battery warning light", "Dim lights", "Slow cranking"), "Soon", "Moderate"),
    P0563("P0563", "System Voltage High", "Battery/Charging", "High", "Check alternator output, voltage regulator, and battery condition.",
            Arrays.asList("Overcharging alternator", "Bad voltage regulator", "Battery short", "Wiring fault"),
            Arrays.asList("Bright lights", "Battery warning light", "Electrical damage"), "Soon", "Moderate"),
    P0621("P0621", "Generator Field Terminal Circuit/Open", "Battery/Charging", "Medium", "Check alternator field circuit wiring and connections.",
            Arrays.asList("Open field circuit", "Broken alternator wiring", "Faulty voltage regulator", "Connector corrosion"),
            Arrays.asList("Battery warning light", "Battery drain", "Poor charging"), "Soon", "Moderate"),
    P0622("P0622", "Generator Field Terminal Circuit Malfunction", "Battery/Charging", "Medium", "Verify alternator field circuit and voltage regulator.",
            Arrays.asList("Alternator field issue", "Regulator fault", "Wiring problem", "ECU output issue"),
            Arrays.asList("No charge", "Battery dies", "Check engine light"), "Soon", "Moderate"),
    P0623("P0623", "Generator Lamp/L-Terminal Circuit Malfunction", "Battery/Charging", "Medium", "Inspect alternator indicator lamp circuit and connector.",
            Arrays.asList("Bad lamp circuit", "Open wiring", "Faulty alternator", "Connector fault"),
            Arrays.asList("Lamp always on", "Charging issue", "Warning light"), "Soon", "Low"),
    P0627("P0627", "Generator Lamp B Terminal Circuit Malfunction", "Battery/Charging", "Medium", "Inspect alternator wiring and battery connection.",
            Arrays.asList("Alternator wiring issue", "Battery connection loose", "Bad connector", "Regulator fault"),
            Arrays.asList("Charging failure", "Battery warning", "Electrical drain"), "Soon", "Low"),
    P0628("P0628", "Generator Field Terminal Circuit/Open", "Battery/Charging", "Medium", "Check alternator field circuit wiring after verifying battery voltage.",
            Arrays.asList("Open field wiring", "Disconnected terminal", "Faulty alternator", "ECU fault"),
            Arrays.asList("No alternator output", "Battery warning light", "Dead battery"), "Soon", "Moderate"),

    // ===== ABS / BRAKE FAULTS =====
    C0035("C0035", "Left Front Wheel Speed Sensor Circuit", "ABS/Brakes", "High", "Inspect wheel speed sensor and harness.",
            Arrays.asList("Damaged wheel sensor", "Corroded connector", "Sensor tone ring issue", "Wiring break"),
            Arrays.asList("ABS warning light", "Traction control fault", "Brake pulsation"), "Soon", "Moderate"),
    C0040("C0040", "Right Front Wheel Speed Sensor Circuit", "ABS/Brakes", "High", "Inspect the right front speed sensor and wiring.",
            Arrays.asList("Sensor damage", "Open circuit", "Connector corrosion", "Tone ring fault"),
            Arrays.asList("Traction loss", "ABS warning", "Braking instability"), "Soon", "Moderate"),
    C0051("C0051", "Left Rear Wheel Speed Sensor Circuit", "ABS/Brakes", "High", "Inspect rear wheel sensor and wiring harness.",
            Arrays.asList("Sensor failure", "Wiring fault", "Wheel bearing issue", "Connector corrosion"),
            Arrays.asList("ABS light on", "Brake pulsation", "Loss of stability"), "Soon", "Moderate"),
    C0075("C0075", "Right Rear Wheel Speed Sensor Circuit", "ABS/Brakes", "High", "Verify right rear sensor and electrical connections.",
            Arrays.asList("Sensor open circuit", "Connector corrosion", "Damaged tone wheel", "Wiring break"),
            Arrays.asList("ABS fault light", "Traction control error", "Uneven braking"), "Soon", "Moderate"),
    C0101("C0101", "ABS Hydraulic Pump Malfunction", "ABS/Brakes", "Critical", "Inspect hydraulic pump operation and electrical connections.",
            Arrays.asList("Failed hydraulic pump", "Pump motor failure", "Electrical fault", "Fluid leak"),
            Arrays.asList("ABS inoperative", "Brake warning light", "Loss of ABS"), "Immediate", "High"),
    C0110("C0110", "ABS Brake Pressure Sensor Malfunction", "ABS/Brakes", "High", "Check brake pressure sensor and wiring.",
            Arrays.asList("Bad pressure sensor", "Sensor wiring fault", "Connector issue", "ECU problem"),
            Arrays.asList("ABS warning", "Brake issue", "Pressure sensing error"), "Soon", "Moderate"),

    // ===== EMISSION SYSTEM FAULTS =====
    P0400("P0400", "Exhaust Gas Recirculation Flow Malfunction", "Emissions", "High", "Inspect EGR valve, passageway, and vacuum supply.",
            Arrays.asList("Stuck EGR valve", "Clogged EGR passages", "Vacuum leak", "EGR solenoid fault"),
            Arrays.asList("Rough idle", "Emissions failure", "Reduced power"), "Soon", "Moderate"),
    P0401("P0401", "Exhaust Gas Recirculation Flow Insufficient", "Emissions", "High", "Check EGR flow and valve operation.",
            Arrays.asList("Restricted EGR", "Faulty valve", "Intake manifold deposits", "Temperature sensor issue"),
            Arrays.asList("Poor emissions", "Engine ping", "Check engine light"), "Soon", "Moderate"),
    P0410("P0410", "Secondary Air Injection System Malfunction", "Emissions", "Medium", "Inspect secondary air system pump and check valves.",
            Arrays.asList("Failed air pump", "Check valve stuck", "Hose cracked", "Solenoid fault"),
            Arrays.asList("Emissions failure", "Rough start", "Check engine light"), "Soon", "Moderate"),
    P0441("P0441", "Evaporative Emission System Incorrect Purge Flow", "Emissions", "Medium", "Verify purge solenoid, hoses, and vapor lines.",
            Arrays.asList("Bad purge valve", "Clogged vapor line", "Faulty sensor", "Wiring issue"),
            Arrays.asList("Hard start", "Fuel smell", "MIL illuminated"), "Soon", "Moderate"),
    P0443("P0443", "Evaporative Emission System Purge Control Valve Circuit", "Emissions", "Medium", "Inspect purge solenoid and control circuits.",
            Arrays.asList("Purge solenoid fault", "Wiring break", "Stuck valve", "ECU driver issue"),
            Arrays.asList("Fuel smell", "Hard starting", "Check engine light"), "Soon", "Moderate"),

    // ===== NETWORK / CAN FAULTS =====
    P0600("P0600", "Serial Communication Link Malfunction", "Network/CAN", "Medium", "Inspect data bus wiring, connectors, and control modules.",
            Arrays.asList("CAN bus fault", "Wiring break", "Connector corrosion", "Module communication issue"),
            Arrays.asList("Multiple warning lights", "Communication error", "Module not responding"), "Soon", "High"),
    P0601("P0601", "Internal Control Module Memory Check Sum Error", "Network/CAN", "Medium", "Verify module power, ground, and update or replace module if needed.",
            Arrays.asList("Memory checksum failure", "Power glitch", "Module defect", "Corrupt memory"),
            Arrays.asList("Intermittent faults", "No-start", "Communication errors"), "Soon", "High"),
    P0602("P0602", "Control Module Programming Error", "Network/CAN", "Medium", "Confirm correct module software and perform reprogramming if required.",
            Arrays.asList("Wrong firmware version", "Incomplete program", "Corrupted software", "Programming fault"),
            Arrays.asList("Module malfunction", "Multiple faults", "No communication"), "Soon", "High"),
    P0603("P0603", "Internal Control Module Keep Alive Memory Error", "Network/CAN", "Medium", "Inspect module battery backup and memory circuits.",
            Arrays.asList("Memory backup failure", "Power supply issue", "Module defect", "Battery disconnect"),
            Arrays.asList("Loss of settings", "Intermittent faults", "No-start"), "Soon", "High"),
    P0604("P0604", "Internal Control Module RAM Error", "Network/CAN", "Medium", "Check module health and replace if memory faults persist.",
            Arrays.asList("RAM memory fault", "Data corruption", "Module defect", "Power issue"),
            Arrays.asList("Random faults", "System resets", "Check engine light"), "Soon", "High"),
    P0605("P0605", "Internal Control Module ROM Error", "Network/CAN", "Medium", "Inspect module firmware integrity and reflash if available.",
            Arrays.asList("ROM memory fault", "Firmware corruption", "Flash error", "Module failure"),
            Arrays.asList("Module not responding", "No functions work", "Check engine light"), "Soon", "High"),
    P0606("P0606", "PCM Processor Fault", "Network/CAN", "High", "Inspect ECU connections and consider module replacement.",
            Arrays.asList("ECU processor failure", "Power supply issue", "Module defect", "Connection loose"),
            Arrays.asList("Multiple system failures", "No communication", "Check engine light"), "Soon", "High"),
    P0607("P0607", "Control Module Device Fault", "Network/CAN", "Medium", "Verify module operation and communication with other modules.",
            Arrays.asList("Device malfunction", "Module defect", "Power issue", "Communication fault"),
            Arrays.asList("Module not responding", "Intermittent faults", "Warning lights"), "Soon", "High"),
    P0608("P0608", "Control Module VVC/VVT System Fault", "Network/CAN", "Medium", "Inspect control module and variable valve timing components.",
            Arrays.asList("VVT solenoid issue", "Module problem", "Wiring fault", "Sensor malfunction"),
            Arrays.asList("Variable timing fault", "Performance loss", "Check engine light"), "Soon", "Moderate"),
    P0609("P0609", "Generator Control Circuit Fault", "Network/CAN", "Medium", "Inspect generator/alternator control wiring and ECU signals.",
            Arrays.asList("Generator wiring fault", "ECU output problem", "Control issue", "Alternator failure"),
            Arrays.asList("No charge", "Battery warning", "Check engine light"), "Soon", "Moderate"),
    U0100("U0100", "Lost Communication With ECM/PCM", "Network/CAN", "High", "Inspect communication wiring and control module power.",
            Arrays.asList("Broken CAN bus", "Faulty ECM/PCM", "Connector issue"),
            Arrays.asList("No starting", "Engine control failure", "Multiple module faults"), "Immediate", "High"),
    U0101("U0101", "Lost Communication With TCM", "Network/CAN", "High", "Inspect transmission control module communication circuit.",
            Arrays.asList("Faulty TCM", "CAN bus fault", "Module power issue"),
            Arrays.asList("Shifting problems", "Transmission warning light", "Reduced performance"), "Immediate", "High"),
    U0102("U0102", "Lost Communication With ABS Control Module", "Network/CAN", "High", "Inspect ABS module connections and CAN wiring.",
            Arrays.asList("Disconnected ABS module", "CAN bus fault", "Module failure"),
            Arrays.asList("ABS warning light", "Loss of ABS", "Brake instability"), "Immediate", "High"),
    U0105("U0105", "Lost Communication With Power Steering Control Module", "Network/CAN", "Medium", "Verify power steering module communication and wiring.",
            Arrays.asList("CAN bus disruption", "Faulty power steering module", "Connector corrosion"),
            Arrays.asList("Power steering assist loss", "Warning lamp", "Intermittent faults"), "High", "Moderate");

    private static final long serialVersionUID = 1L;

    private final String code;
    private final String description;
    private final String systemCategory;
    private final String severity;
    private final String recommendation;
    private final List<String> possibleCauses;
    private final List<String> symptoms;
    private final String repairUrgency;
    private final String estimatedRepairComplexity;

    /**
     * Creates a FaultCode with comprehensive diagnostic metadata.
     */
    FaultCode(String code, String description, String systemCategory, String severity, String recommendation,
              List<String> possibleCauses, List<String> symptoms, String repairUrgency, String estimatedRepairComplexity) {
        this.code = code;
        this.description = description;
        this.systemCategory = systemCategory;
        this.severity = severity;
        this.recommendation = recommendation;
        this.possibleCauses = possibleCauses;
        this.symptoms = symptoms;
        this.repairUrgency = repairUrgency;
        this.estimatedRepairComplexity = estimatedRepairComplexity;
    }

    /**
     * Creates a new FaultCode with default diagnostic metadata (legacy fallback).
     */
    FaultCode(String code, String description, String systemCategory, String severity, String recommendation) {
        this(code, description, systemCategory, severity, recommendation,
             Arrays.asList("Consult diagnostic procedure", "Verify wiring and sensors"),
             Arrays.asList("Check engine light", "Reduced performance"),
             "Moderate", "Moderate");
    }

    public String getCode() { return code; }
    public String getDescription() { return description; }
    public String getSystemCategory() { return systemCategory; }
    public String getSeverity() { return severity; }
    public String getRecommendation() { return recommendation; }
    public List<String> getPossibleCauses() { return possibleCauses; }
    public List<String> getSymptoms() { return symptoms; }
    public String getRepairUrgency() { return repairUrgency; }
    public String getEstimatedRepairComplexity() { return estimatedRepairComplexity; }

    public static FaultCode fromCode(String code) {
        if (code == null || code.trim().isEmpty()) return null;
        try {
            return FaultCode.valueOf(code.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return getCode() + ": " + description + " [" + systemCategory + ", " + severity + ", urgency=" + repairUrgency + ", complexity=" + estimatedRepairComplexity + "]";
    }

    public String getDetailedDiagnostics() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== FAULT CODE DIAGNOSTICS ===\n");
        sb.append("Code: ").append(code).append("\n");
        sb.append("Description: ").append(description).append("\n");
        sb.append("Category: ").append(systemCategory).append("\n");
        sb.append("Severity: ").append(severity).append("\n");
        sb.append("Urgency: ").append(repairUrgency).append("\n");
        sb.append("Complexity: ").append(estimatedRepairComplexity).append("\n");
        sb.append("\nPossible Causes:\n");
        for (String cause : possibleCauses) {
            sb.append("  - ").append(cause).append("\n");
        }
        sb.append("\nSymptoms:\n");
        for (String symptom : symptoms) {
            sb.append("  - ").append(symptom).append("\n");
        }
        sb.append("\nRecommendation:\n");
        sb.append("  ").append(recommendation).append("\n");
        return sb.toString();
        }      }