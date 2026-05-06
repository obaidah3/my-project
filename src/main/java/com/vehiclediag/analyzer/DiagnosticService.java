package com.vehiclediag.analyzer;

import com.vehiclediag.model.DiagnosticReport;
import com.vehiclediag.model.DiagnosticRequest;
import com.vehiclediag.model.FaultCode;

/**
 * Service for processing diagnostic requests and generating diagnostic reports.
 * Orchestrates all diagnostic analyzers (engine, battery, fuel, fault code)
 * to produce a comprehensive diagnostic analysis.
 */
public class DiagnosticService {

    private final EngineAnalyzer engineAnalyzer;
    private final BatteryAnalyzer batteryAnalyzer;
    private final FuelAnalyzer fuelAnalyzer;
    private final FaultCodeAnalyzer faultCodeAnalyzer;
    private final OilPressureAnalyzer oilPressureAnalyzer;
    private final CoolingSystemAnalyzer coolingSystemAnalyzer;
    private final TransmissionAnalyzer transmissionAnalyzer;
    private final EmissionSensorAnalyzer emissionSensorAnalyzer;
    private final DrivingBehaviorAnalyzer drivingBehaviorAnalyzer;

    /**
     * Creates a new DiagnosticService with all required analyzers.
     */
    public DiagnosticService() {
        this.engineAnalyzer = new EngineAnalyzer();
        this.batteryAnalyzer = new BatteryAnalyzer();
        this.fuelAnalyzer = new FuelAnalyzer();
        this.faultCodeAnalyzer = new FaultCodeAnalyzer();
        this.oilPressureAnalyzer = new OilPressureAnalyzer();
        this.coolingSystemAnalyzer = new CoolingSystemAnalyzer();
        this.transmissionAnalyzer = new TransmissionAnalyzer();
        this.emissionSensorAnalyzer = new EmissionSensorAnalyzer();
        this.drivingBehaviorAnalyzer = new DrivingBehaviorAnalyzer();
    }

    /**
     * Processes a diagnostic request by invoking all analyzers and combining their results
     * into a comprehensive diagnostic report.
     *
     * @param request the diagnostic request containing vehicle and sensor data
     * @return a complete diagnostic report with analysis results
     * @throws IllegalArgumentException if the request is null
     */
    public DiagnosticReport processDiagnosticRequest(DiagnosticRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("DiagnosticRequest cannot be null");
        }

        // Invoke each analyzer polymorphically
        AnalysisResult engineResult = engineAnalyzer.analyze(request);
        AnalysisResult batteryResult = batteryAnalyzer.analyze(request);
        AnalysisResult fuelResult = fuelAnalyzer.analyze(request);

        // Process fault code information
        AnalysisResult faultCodeResult = faultCodeAnalyzer.analyze(request);
        String faultDetails = faultCodeResult.getMessage();

        // Analyze additional systems
        AnalysisResult oilPressureResult = oilPressureAnalyzer.analyze(request);
        AnalysisResult coolingSystemResult = coolingSystemAnalyzer.analyze(request);
        AnalysisResult transmissionResult = transmissionAnalyzer.analyze(request);
        AnalysisResult emissionSensorResult = emissionSensorAnalyzer.analyze(request);
        AnalysisResult drivingBehaviorResult = drivingBehaviorAnalyzer.analyze(request);

        // Determine overall diagnostic summary
        String overallSummary = determineOverallSummary(engineResult, batteryResult, fuelResult, faultCodeResult,
                                                        oilPressureResult, coolingSystemResult, transmissionResult,
                                                        emissionSensorResult, drivingBehaviorResult);

        // Calculate vehicle health score based on analyzer severities
        int healthScore = calculateHealthScore(engineResult, batteryResult, fuelResult, faultCodeResult,
                                              oilPressureResult, coolingSystemResult, transmissionResult,
                                              emissionSensorResult, drivingBehaviorResult);

        // Create and return the diagnostic report
        return new DiagnosticReport(
                request.getVehicle(),
                request.getSpeed(),
                request.getRpm(),
                request.getEngineTemperature(),
                request.getBatteryVoltage(),
                request.getFuelLevel(),
                request.getFaultCode(),
                engineResult.getCondition(),
                batteryResult.getCondition(),
                fuelResult.getCondition(),
                oilPressureResult.getCondition(),
                coolingSystemResult.getCondition(),
                transmissionResult.getCondition(),
                emissionSensorResult.getCondition(),
                drivingBehaviorResult.getCondition(),
                overallSummary,
                faultDetails,
                healthScore,
                request.getOilPressure(),
                request.getCoolantLevel(),
                request.getTransmissionTemperature(),
                request.getThrottlePosition(),
                request.getMafReading(),
                request.getOxygenSensorVoltage(),
                request.getMileage()
        );
    }

    /**
     * Converts a fault code string to human-readable fault details.
     * Uses the FaultCode enum to look up known fault codes.
     *
     * @param faultCodeString the fault code string (e.g., "P0001")
     * @return formatted fault details, empty string if no fault code, or unknown fault message
     */
    private String getFaultDetails(String faultCodeString) {
        // No fault code provided
        if (faultCodeString == null || faultCodeString.trim().isEmpty()) {
            return "";
        }

        // Try to find the fault code in the FaultCode enum
        FaultCode faultCode = FaultCode.fromCode(faultCodeString);

        if (faultCode != null) {
            // Known fault code - return formatted description
            return faultCode.getCode() + ": " + faultCode.getDescription();
        }

        // Unknown fault code - return generic message
        return "Unknown fault code: " + faultCodeString;
    }

    /**
     * Determines the overall diagnostic summary based on the severity of individual analyzer results.
     * Priority: Critical > Warning > Moderate > Good
     *
     * @param engineResult the engine analysis result
     * @param batteryResult the battery analysis result
     * @param fuelResult the fuel analysis result
     * @return overall diagnostic summary string
     */
    private int calculateHealthScore(AnalysisResult... results) {
        int score = 100;
        if (results == null) {
            return score;
        }

        for (AnalysisResult result : results) {
            score -= getSeverityDeduction(result != null ? result.getSeverity() : "LOW");
        }

        if (score < 0) {
            score = 0;
        }
        return score;
    }

    private int getSeverityDeduction(String severity) {
        if (severity == null) {
            return 0;
        }
        switch (severity.trim().toUpperCase()) {
            case "CRITICAL":
                return 35;
            case "HIGH":
                return 25;
            case "MEDIUM":
                return 15;
            default:
                return 0;
        }
    }

    private String determineOverallSummary(AnalysisResult engineResult,
                                          AnalysisResult batteryResult,
                                          AnalysisResult fuelResult,
                                          AnalysisResult faultCodeResult,
                                          AnalysisResult oilPressureResult,
                                          AnalysisResult coolingSystemResult,
                                          AnalysisResult transmissionResult,
                                          AnalysisResult emissionSensorResult,
                                          AnalysisResult drivingBehaviorResult) {
        // Check for CRITICAL severity (highest priority)
        if ("CRITICAL".equals(engineResult.getSeverity()) ||
            "CRITICAL".equals(batteryResult.getSeverity()) ||
            "CRITICAL".equals(fuelResult.getSeverity()) ||
            "CRITICAL".equals(faultCodeResult.getSeverity()) ||
            "CRITICAL".equals(oilPressureResult.getSeverity()) ||
            "CRITICAL".equals(coolingSystemResult.getSeverity()) ||
            "CRITICAL".equals(transmissionResult.getSeverity()) ||
            "CRITICAL".equals(emissionSensorResult.getSeverity()) ||
            "CRITICAL".equals(drivingBehaviorResult.getSeverity())) {
            return "CRITICAL: Vehicle requires immediate attention. Multiple systems need service.";
        }

        // Check for HIGH severity
        if ("HIGH".equals(engineResult.getSeverity()) ||
            "HIGH".equals(batteryResult.getSeverity()) ||
            "HIGH".equals(fuelResult.getSeverity()) ||
            "HIGH".equals(faultCodeResult.getSeverity()) ||
            "HIGH".equals(oilPressureResult.getSeverity()) ||
            "HIGH".equals(coolingSystemResult.getSeverity()) ||
            "HIGH".equals(transmissionResult.getSeverity()) ||
            "HIGH".equals(emissionSensorResult.getSeverity()) ||
            "HIGH".equals(drivingBehaviorResult.getSeverity())) {
            return "WARNING: Vehicle has critical conditions. Service required soon.";
        }

        // Check for MEDIUM severity
        if ("MEDIUM".equals(engineResult.getSeverity()) ||
            "MEDIUM".equals(batteryResult.getSeverity()) ||
            "MEDIUM".equals(fuelResult.getSeverity()) ||
            "MEDIUM".equals(faultCodeResult.getSeverity()) ||
            "MEDIUM".equals(oilPressureResult.getSeverity()) ||
            "MEDIUM".equals(coolingSystemResult.getSeverity()) ||
            "MEDIUM".equals(transmissionResult.getSeverity()) ||
            "MEDIUM".equals(emissionSensorResult.getSeverity()) ||
            "MEDIUM".equals(drivingBehaviorResult.getSeverity())) {
            return "MODERATE: Some vehicle systems need attention. Schedule service soon.";
        }

        // All analyzers report LOW severity
        return "GOOD: Vehicle is operating normally. No immediate service required.";
    }
}