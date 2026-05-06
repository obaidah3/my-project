package com.vehiclediag.analyzer;

import com.vehiclediag.model.DiagnosticRequest;
import com.vehiclediag.util.Constants;

/**
 * Analyzer for vehicle driving behavior and patterns.
 * Monitors throttle usage, acceleration patterns, and RPM management.
 */
public class DrivingBehaviorAnalyzer implements Analyzer {

    /**
     * Analyzes the vehicle's driving behavior based on throttle position, speed, and RPM.
     *
     * @param request the diagnostic request containing driving behavior data
     * @return analysis result with condition and severity
     */
    @Override
    public AnalysisResult analyze(DiagnosticRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("DiagnosticRequest cannot be null");
        }

        double throttlePosition = request.getThrottlePosition();
        double speed = request.getSpeed();
        int rpm = request.getRpm();
        int mileage = request.getMileage();

        // Check for aggressive driving patterns
        if (throttlePosition > Constants.THROTTLE_AGGRESSIVE_MIN) {
            if (rpm > Constants.ENGINE_CRITICAL_RPM_MIN) {
                return AnalysisResult.warning(
                        "Aggressive Driving Detected",
                        "High throttle (" + String.format("%.1f", throttlePosition) + "%) and " +
                        "high RPM (" + rpm + ") detected. Aggressive driving increases fuel consumption " +
                        "and component wear. Adopt smoother acceleration patterns.");
            } else if (rpm > Constants.ENGINE_MODERATE_RPM_MAX) {
                return AnalysisResult.warning(
                        "Moderate Driving Aggression",
                        "Elevated throttle (" + String.format("%.1f", throttlePosition) + "%) with " +
                        "moderate RPM (" + rpm + "). Consider gentler acceleration.");
            }
        }

        // Check for excessive RPM at low speed (engine rev-ing)
        if (speed < 20 && rpm > Constants.ENGINE_MODERATE_RPM_MAX) {
            return AnalysisResult.warning(
                    "Excessive Engine Revving",
                    "Engine RPM (" + rpm + ") is high for current speed (" + 
                    String.format("%.1f", speed) + " km/h). Rev-limiting may reduce engine life.");
        }

        // Check for high mileage with aggressive usage
        if (mileage > 150000 && throttlePosition > Constants.THROTTLE_AGGRESSIVE_MIN) {
            return AnalysisResult.warning(
                    "High-Mileage Aggressive Driving",
                    "Vehicle with high mileage (" + mileage + " km) exhibiting aggressive " +
                    "driving patterns. Prioritize smooth driving to extend component life.");
        }

        // Normal driving behavior
        String behavior = describeDrivingBehavior(throttlePosition, speed, rpm);
        return AnalysisResult.success(
                "Normal Driving Behavior",
                "Driving patterns are within normal parameters. " + behavior);
    }

    private String describeDrivingBehavior(double throttlePosition, double speed, int rpm) {
        if (throttlePosition < 20) {
            return "Vehicle is operating in a very conservative manner.";
        } else if (throttlePosition < 50) {
            return "Vehicle is being driven conservatively with moderate throttle usage.";
        } else if (throttlePosition < 80) {
            return "Vehicle is being driven with moderate to elevated throttle application.";
        } else {
            return "Vehicle is being driven with sustained moderate throttle.";
        }
    }
}
