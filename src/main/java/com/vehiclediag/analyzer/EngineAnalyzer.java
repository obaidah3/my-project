package com.vehiclediag.analyzer;

import com.vehiclediag.model.DiagnosticRequest;
import com.vehiclediag.util.Constants;

/**
 * Analyzer for engine-related diagnostic parameters.
 * Evaluates RPM and engine temperature to determine engine condition.
 */
public class EngineAnalyzer implements Analyzer {

    /**
     * Analyzes engine parameters from the diagnostic request.
     * Evaluates RPM and temperature against predefined thresholds.
     *
     * @param request the diagnostic request containing engine data
     * @return analysis result with engine condition assessment
     */
    @Override
    public AnalysisResult analyze(DiagnosticRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("DiagnosticRequest cannot be null");
        }

        int rpm = request.getRpm();
        double temperature = request.getEngineTemperature();

        // Check for critical conditions first
        if (rpm >= Constants.ENGINE_CRITICAL_RPM_MIN || temperature >= Constants.ENGINE_CRITICAL_TEMP_MIN) {
            String message = String.format("Engine requires immediate attention. RPM: %d, Temperature: %.1f°C. " +
                    "Stop vehicle immediately and consult mechanic.", rpm, temperature);
            return AnalysisResult.emergency("Critical Condition", message);
        }

        // Check for moderate conditions
        if (rpm > Constants.ENGINE_GOOD_RPM_MAX || temperature > Constants.ENGINE_GOOD_TEMP_MAX) {
            String message = String.format("Engine showing elevated parameters. RPM: %d, Temperature: %.1f°C. " +
                    "Monitor closely and service soon.", rpm, temperature);
            return AnalysisResult.warning("Moderate Condition", message);
        }

        // Good condition
        String message = String.format("Engine operating normally. RPM: %d, Temperature: %.1f°C. " +
                "No immediate action required.", rpm, temperature);
        return AnalysisResult.success("Good Condition", message);
    }
}