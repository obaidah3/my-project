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

    /**
     * Creates a new DiagnosticService with all required analyzers.
     */
    public DiagnosticService() {
        this.engineAnalyzer = new EngineAnalyzer();
        this.batteryAnalyzer = new BatteryAnalyzer();
        this.fuelAnalyzer = new FuelAnalyzer();
        this.faultCodeAnalyzer = new FaultCodeAnalyzer();
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

        // Determine overall diagnostic summary
        String overallSummary = determineOverallSummary(engineResult, batteryResult, fuelResult, faultCodeResult);

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
                overallSummary,
                faultDetails
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
    private String determineOverallSummary(AnalysisResult engineResult,
                                          AnalysisResult batteryResult,
                                          AnalysisResult fuelResult,
                                          AnalysisResult faultCodeResult) {
        // Check for CRITICAL severity (highest priority)
        if ("CRITICAL".equals(engineResult.getSeverity()) ||
            "CRITICAL".equals(batteryResult.getSeverity()) ||
            "CRITICAL".equals(fuelResult.getSeverity()) ||
            "CRITICAL".equals(faultCodeResult.getSeverity())) {
            return "CRITICAL: Vehicle requires immediate attention. Multiple systems need service.";
        }

        // Check for HIGH severity
        if ("HIGH".equals(engineResult.getSeverity()) ||
            "HIGH".equals(batteryResult.getSeverity()) ||
            "HIGH".equals(fuelResult.getSeverity()) ||
            "HIGH".equals(faultCodeResult.getSeverity())) {
            return "WARNING: Vehicle has critical conditions. Service required soon.";
        }

        // Check for MEDIUM severity
        if ("MEDIUM".equals(engineResult.getSeverity()) ||
            "MEDIUM".equals(batteryResult.getSeverity()) ||
            "MEDIUM".equals(fuelResult.getSeverity()) ||
            "MEDIUM".equals(faultCodeResult.getSeverity())) {
            return "MODERATE: Some vehicle systems need attention. Schedule service soon.";
        }

        // All analyzers report LOW severity
        return "GOOD: Vehicle is operating normally. No immediate service required.";
    }
}