package com.vehiclediag.analyzer;

import com.vehiclediag.model.DiagnosticRequest;
import com.vehiclediag.util.Constants;

/**
 * Analyzer for battery voltage diagnostic parameters.
 * Evaluates battery voltage to determine battery health condition.
 */
public class BatteryAnalyzer implements Analyzer {

    /**
     * Analyzes battery voltage from the diagnostic request.
     * Evaluates voltage against predefined thresholds.
     *
     * @param request the diagnostic request containing battery data
     * @return analysis result with battery condition assessment
     */
    @Override
    public AnalysisResult analyze(DiagnosticRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("DiagnosticRequest cannot be null");
        }

        double voltage = request.getBatteryVoltage();

        // Check for critical condition (too low)
        if (voltage < Constants.BATTERY_CRITICAL_MAX_V) {
            String message = String.format("Battery voltage critically low: %.1fV. " +
                    "Battery may be failing. Replace battery immediately.", voltage);
            return AnalysisResult.emergency("Critical Condition", message);
        }

        // Check for acceptable condition
        if (voltage >= Constants.BATTERY_ACCEPTABLE_MIN_V && voltage < Constants.BATTERY_GOOD_MIN_V) {
            String message = String.format("Battery voltage acceptable but could be better: %.1fV. " +
                    "Consider charging or testing battery health.", voltage);
            return AnalysisResult.warning("Acceptable Condition", message);
        }

        // Check for good condition
        if (voltage >= Constants.BATTERY_GOOD_MIN_V && voltage <= Constants.BATTERY_GOOD_MAX_V) {
            String message = String.format("Battery voltage in optimal range: %.1fV. " +
                    "Battery health is good.", voltage);
            return AnalysisResult.success("Good Condition", message);
        }

        // Voltage too high (above good range)
        String message = String.format("Battery voltage unusually high: %.1fV. " +
                "Check charging system and battery health.", voltage);
        return AnalysisResult.warning("High Voltage", message);
    }
}