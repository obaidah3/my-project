package com.vehiclediag.analyzer;

import com.vehiclediag.model.DiagnosticRequest;
import com.vehiclediag.util.Constants;

/**
 * Analyzer for vehicle oil pressure conditions.
 * Monitors oil pressure levels and identifies lubrication system issues.
 */
public class OilPressureAnalyzer implements Analyzer {

    /**
     * Analyzes the vehicle's oil pressure reading.
     *
     * @param request the diagnostic request containing oil pressure data
     * @return analysis result with condition and severity
     */
    @Override
    public AnalysisResult analyze(DiagnosticRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("DiagnosticRequest cannot be null");
        }

        double oilPressure = request.getOilPressure();

        // Check for critical low oil pressure
        if (oilPressure < Constants.OIL_PRESSURE_CRITICAL_MIN) {
            return AnalysisResult.emergency(
                    "Critical Oil Pressure",
                    "Oil pressure is dangerously low (" + String.format("%.1f", oilPressure) + " bar). " +
                    "Engine damage risk. Stop engine immediately and check oil level and pump.");
        }

        // Check for low oil pressure
        if (oilPressure < Constants.OIL_PRESSURE_LOW_MIN) {
            return AnalysisResult.critical(
                    "Low Oil Pressure",
                    "Oil pressure is below normal (" + String.format("%.1f", oilPressure) + " bar). " +
                    "Verify oil level, filter condition, and pump operation.");
        }

        // Check for high oil pressure
        if (oilPressure > Constants.OIL_PRESSURE_GOOD_MAX) {
            return AnalysisResult.warning(
                    "Elevated Oil Pressure",
                    "Oil pressure is elevated (" + String.format("%.1f", oilPressure) + " bar). " +
                    "Check for restricted oil lines or excessive engine load.");
        }

        // Normal oil pressure
        return AnalysisResult.success(
                "Oil Pressure Normal",
                "Oil pressure is within normal operating range (" + String.format("%.1f", oilPressure) + " bar).");
    }
}
