package com.vehiclediag.analyzer;

import com.vehiclediag.model.DiagnosticRequest;
import com.vehiclediag.util.Constants;

/**
 * Analyzer for fuel level diagnostic parameters.
 * Evaluates fuel level to determine if refueling is needed.
 */
public class FuelAnalyzer implements Analyzer {

    /**
     * Analyzes fuel level from the diagnostic request.
     * Evaluates fuel percentage against warning threshold.
     *
     * @param request the diagnostic request containing fuel data
     * @return analysis result with fuel condition assessment
     */
    @Override
    public AnalysisResult analyze(DiagnosticRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("DiagnosticRequest cannot be null");
        }

        double fuelLevel = request.getFuelLevel();

        // Check for low fuel warning
        if (fuelLevel < Constants.FUEL_WARNING_THRESHOLD) {
            String message = String.format("Fuel level low: %.1f%%. " +
                    "Refuel immediately to avoid running out of fuel.", fuelLevel);
            return AnalysisResult.critical("Low Fuel", message);
        }

        // Normal fuel level
        String message = String.format("Fuel level adequate: %.1f%%. " +
                "No refueling required at this time.", fuelLevel);
        return AnalysisResult.success("Normal", message);
    }
}