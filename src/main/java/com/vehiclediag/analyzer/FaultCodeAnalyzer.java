package com.vehiclediag.analyzer;

import com.vehiclediag.model.DiagnosticRequest;
import com.vehiclediag.model.FaultCode;

/**
 * Analyzer for reported fault codes.
 * Uses the FaultCode enum to resolve known OBD-II trouble codes
 * and returns a severity-aware analysis result.
 */
public class FaultCodeAnalyzer implements Analyzer {

    /**
     * Analyzes the reported fault code from the diagnostic request.
     *
     * @param request the diagnostic request containing the fault code
     * @return analysis result with condition, message, and severity
     */
    @Override
    public AnalysisResult analyze(DiagnosticRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("DiagnosticRequest cannot be null");
        }

        String faultCodeString = request.getFaultCode();
        if (faultCodeString == null || faultCodeString.trim().isEmpty()) {
            return AnalysisResult.success(
                    "No Fault Code Provided",
                    "No diagnostic trouble code was reported. No fault-code based analysis is required.");
        }

        FaultCode faultCode = FaultCode.fromCode(faultCodeString);
        if (faultCode == null) {
            return AnalysisResult.warning(
                    "Unknown Fault Code",
                    "Unknown fault code: " + faultCodeString.trim().toUpperCase() + ". Verify the code and consult manufacturer documentation.");
        }

        String condition = getConditionForSeverity(faultCode.getSeverity());
        String message = buildFaultCodeMessage(faultCode);

        switch (faultCode.getSeverity().trim().toUpperCase()) {
            case "CRITICAL":
                return AnalysisResult.emergency(condition, message);
            case "HIGH":
                return AnalysisResult.critical(condition, message);
            case "MEDIUM":
                return AnalysisResult.warning(condition, message);
            default:
                return AnalysisResult.success(condition, message);
        }
    }

    private String buildFaultCodeMessage(FaultCode faultCode) {
        StringBuilder sb = new StringBuilder();
        sb.append(faultCode.getCode()).append(": ").append(faultCode.getDescription()).append("\n");
        sb.append("Category: ").append(faultCode.getSystemCategory()).append("\n");
        sb.append("Severity: ").append(faultCode.getSeverity()).append("\n");
        sb.append("Repair Urgency: ").append(faultCode.getRepairUrgency()).append("\n");
        sb.append("Estimated Complexity: ").append(faultCode.getEstimatedRepairComplexity()).append("\n");
        
        sb.append("\nPossible Causes:\n");
        for (String cause : faultCode.getPossibleCauses()) {
            sb.append("  - ").append(cause).append("\n");
        }
        
        sb.append("\nSymptoms:\n");
        for (String symptom : faultCode.getSymptoms()) {
            sb.append("  - ").append(symptom).append("\n");
        }
        
        sb.append("\nRecommended Action:\n");
        sb.append(faultCode.getRecommendation());
        
        return sb.toString();
    }

    private String getConditionForSeverity(String severity) {
        if (severity == null || severity.trim().isEmpty()) {
            return "Fault Code Condition";
        }

        switch (severity.trim().toUpperCase()) {
            case "CRITICAL":
                return "Critical Fault Detected";
            case "HIGH":
                return "High Severity Fault";
            case "MEDIUM":
                return "Moderate Fault";
            case "LOW":
                return "Low Severity Fault";
            default:
                return "Fault Code Condition";
        }
    }
}
