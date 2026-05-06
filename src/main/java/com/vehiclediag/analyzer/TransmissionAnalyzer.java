package com.vehiclediag.analyzer;

import com.vehiclediag.model.DiagnosticRequest;
import com.vehiclediag.util.Constants;

/**
 * Analyzer for vehicle transmission conditions.
 * Monitors transmission temperature and operating patterns.
 */
public class TransmissionAnalyzer implements Analyzer {

    /**
     * Analyzes the vehicle's transmission health based on temperature and operating conditions.
     *
     * @param request the diagnostic request containing transmission data
     * @return analysis result with condition and severity
     */
    @Override
    public AnalysisResult analyze(DiagnosticRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("DiagnosticRequest cannot be null");
        }

        double transmissionTemp = request.getTransmissionTemperature();
        int rpm = request.getRpm();
        double throttlePosition = request.getThrottlePosition();

        // Check for excessive transmission temperature (overheating)
        if (transmissionTemp > Constants.TRANSMISSION_MODERATE_TEMP_MAX) {
            String message = "Transmission temperature is critically high (" + 
                            String.format("%.1f", transmissionTemp) + "°C). ";
            if (throttlePosition > Constants.THROTTLE_AGGRESSIVE_MIN) {
                message += "Reduce aggressive driving and allow transmission to cool.";
            } else {
                message += "Check transmission cooler and fluid condition.";
            }
            return AnalysisResult.emergency(
                    "Critical Transmission Temperature",
                    message);
        }

        // Check for elevated transmission temperature
        if (transmissionTemp > Constants.TRANSMISSION_GOOD_TEMP_MAX) {
            String message = "Transmission temperature elevated (" + 
                            String.format("%.1f", transmissionTemp) + "°C). ";
            if (rpm > Constants.ENGINE_MODERATE_RPM_MAX) {
                message += "High engine RPM may be causing transmission stress. Consider reducing load.";
            } else {
                message += "Monitor transmission cooler effectiveness.";
            }
            return AnalysisResult.warning(
                    "Elevated Transmission Temperature",
                    message);
        }

        // Check for aggressive transmission usage
        if (throttlePosition > Constants.THROTTLE_AGGRESSIVE_MIN && rpm > Constants.ENGINE_MODERATE_RPM_MAX) {
            return AnalysisResult.warning(
                    "Aggressive Transmission Stress",
                    "High throttle and high RPM indicate aggressive transmission usage. " +
                    "Consider smoother acceleration to reduce wear.");
        }

        // Transmission is operating normally
        return AnalysisResult.success(
                "Transmission Healthy",
                "Transmission temperature (" + String.format("%.1f", transmissionTemp) + "°C) and " +
                "operating patterns are normal.");
    }
}
