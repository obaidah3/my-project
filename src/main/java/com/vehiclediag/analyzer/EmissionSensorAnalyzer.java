package com.vehiclediag.analyzer;

import com.vehiclediag.model.DiagnosticRequest;
import com.vehiclediag.util.Constants;

/**
 * Analyzer for vehicle emission sensor conditions.
 * Monitors oxygen sensor voltage and mass air flow readings.
 */
public class EmissionSensorAnalyzer implements Analyzer {

    /**
     * Analyzes the vehicle's emission sensors based on oxygen sensor voltage and MAF readings.
     *
     * @param request the diagnostic request containing emission sensor data
     * @return analysis result with condition and severity
     */
    @Override
    public AnalysisResult analyze(DiagnosticRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("DiagnosticRequest cannot be null");
        }

        double oxygenSensorVoltage = request.getOxygenSensorVoltage();
        double mafReading = request.getMafReading();

        // Evaluate oxygen sensor
        String o2Status = evaluateOxygenSensor(oxygenSensorVoltage);
        int o2Severity = getOxygenSensorSeverity(oxygenSensorVoltage);

        // Evaluate MAF sensor
        String mafStatus = evaluateMafReading(mafReading);
        int mafSeverity = getMafSeverity(mafReading);

        // Determine overall severity
        int overallSeverity = Math.max(o2Severity, mafSeverity);

        // Build message
        String message = "O2 Sensor: " + o2Status + " (" + 
                        String.format("%.2f", oxygenSensorVoltage) + "V). " +
                        "MAF Reading: " + mafStatus + " (" + 
                        String.format("%.1f", mafReading) + " g/s).";

        if (o2Severity > 1 || mafSeverity > 1) {
            message += " Check sensor wiring and integrity.";
        }

        return createResult(overallSeverity, message);
    }

    private String evaluateOxygenSensor(double voltage) {
        if (voltage < Constants.O2_SENSOR_GOOD_MIN) {
            return "Low (Rich mixture)";
        } else if (voltage > Constants.O2_SENSOR_GOOD_MAX) {
            return "High (Lean mixture)";
        } else {
            return "Normal";
        }
    }

    private int getOxygenSensorSeverity(double voltage) {
        if (voltage < 0.1 || voltage > 0.9) {
            return 3; // CRITICAL
        } else if (voltage < Constants.O2_SENSOR_GOOD_MIN || voltage > Constants.O2_SENSOR_GOOD_MAX) {
            return 2; // WARNING
        } else {
            return 0; // GOOD
        }
    }

    private String evaluateMafReading(double reading) {
        if (reading < 1.0) {
            return "Very Low (possible sensor fault)";
        } else if (reading < Constants.MAF_READING_GOOD_MIN) {
            return "Low";
        } else if (reading > Constants.MAF_READING_GOOD_MAX) {
            return "High";
        } else {
            return "Normal";
        }
    }

    private int getMafSeverity(double reading) {
        if (reading < 0.5) {
            return 3; // CRITICAL
        } else if (reading < Constants.MAF_READING_GOOD_MIN || reading > Constants.MAF_READING_GOOD_MAX) {
            return 2; // WARNING
        } else {
            return 0; // GOOD
        }
    }

    private AnalysisResult createResult(int severity, String message) {
        switch (severity) {
            case 3:
                return AnalysisResult.emergency("Emission Sensor Critical", message);
            case 2:
                return AnalysisResult.warning("Emission Sensor Warning", message);
            default:
                return AnalysisResult.success("Emission Sensors Normal", message);
        }
    }
}
