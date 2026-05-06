package com.vehiclediag.analyzer;

import com.vehiclediag.model.DiagnosticRequest;
import com.vehiclediag.util.Constants;

/**
 * Analyzer for vehicle cooling system conditions.
 * Monitors coolant level and transmission temperature for cooling effectiveness.
 */
public class CoolingSystemAnalyzer implements Analyzer {

    /**
     * Analyzes the vehicle's cooling system health based on coolant level and transmission temperature.
     *
     * @param request the diagnostic request containing coolant and temperature data
     * @return analysis result with condition and severity
     */
    @Override
    public AnalysisResult analyze(DiagnosticRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("DiagnosticRequest cannot be null");
        }

        double coolantLevel = request.getCoolantLevel();
        double transmissionTemp = request.getTransmissionTemperature();

        // Check for critically low coolant level
        if (coolantLevel < Constants.COOLANT_ACCEPTABLE_MIN) {
            return AnalysisResult.emergency(
                    "Critical Coolant Level",
                    "Coolant level critically low (" + String.format("%.1f", coolantLevel) + "%). " +
                    "Risk of engine overheating. Check for leaks and refill immediately.");
        }

        // Check for low coolant level
        if (coolantLevel < Constants.COOLANT_GOOD_MIN) {
            return AnalysisResult.critical(
                    "Low Coolant Level",
                    "Coolant level below optimal (" + String.format("%.1f", coolantLevel) + "%). " +
                    "Inspect cooling system for leaks and top up coolant.");
        }

        // Check for excessive transmission temperature
        if (transmissionTemp > Constants.TRANSMISSION_MODERATE_TEMP_MAX) {
            return AnalysisResult.critical(
                    "High Transmission Temperature",
                    "Transmission temperature is excessive (" + String.format("%.1f", transmissionTemp) + "°C). " +
                    "Cooling system may be failing. Check fluid level and cooler operation.");
        }

        // Check for elevated transmission temperature
        if (transmissionTemp > Constants.TRANSMISSION_GOOD_TEMP_MAX) {
            return AnalysisResult.warning(
                    "Elevated Transmission Temperature",
                    "Transmission temperature is elevated (" + String.format("%.1f", transmissionTemp) + "°C). " +
                    "Verify cooling fan operation and transmission fluid level.");
        }

        // Cooling system is healthy
        return AnalysisResult.success(
                "Cooling System Healthy",
                "Coolant level (" + String.format("%.1f", coolantLevel) + "%) and " +
                "transmission temperature (" + String.format("%.1f", transmissionTemp) + "°C) are normal.");
    }
}
