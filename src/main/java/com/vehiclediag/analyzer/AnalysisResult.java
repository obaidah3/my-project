package com.vehiclediag.analyzer;

import java.io.Serializable;

/**
 * Represents the result of a diagnostic analysis performed by an analyzer.
 * Contains the condition assessment and any additional details or recommendations.
 */
public class AnalysisResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String condition;
    private final String message;
    private final String severity;

    /**
     * Creates a new AnalysisResult.
     *
     * @param condition the primary condition assessment (e.g., "Good Condition", "Critical")
     * @param message detailed message with explanation and recommendations
     * @param severity the severity level ("LOW", "MEDIUM", "HIGH", "CRITICAL")
     */
    public AnalysisResult(String condition, String message, String severity) {
        if (condition == null || condition.trim().isEmpty()) {
            throw new IllegalArgumentException("Condition cannot be null or empty");
        }
        if (message == null) {
            throw new IllegalArgumentException("Message cannot be null");
        }
        if (severity == null || severity.trim().isEmpty()) {
            throw new IllegalArgumentException("Severity cannot be null or empty");
        }

        this.condition = condition.trim();
        this.message = message;
        this.severity = severity.trim().toUpperCase();
    }

    /**
     * Returns the primary condition assessment.
     *
     * @return the condition string
     */
    public String getCondition() {
        return condition;
    }

    /**
     * Returns the detailed message with explanation and recommendations.
     *
     * @return the detailed message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Returns the severity level of this analysis result.
     *
     * @return the severity ("LOW", "MEDIUM", "HIGH", "CRITICAL")
     */
    public String getSeverity() {
        return severity;
    }

    /**
     * Checks if this result indicates a critical condition.
     *
     * @return true if severity is "CRITICAL" or "HIGH"
     */
    public boolean isCritical() {
        return "CRITICAL".equals(severity) || "HIGH".equals(severity);
    }

    /**
     * Returns a formatted string representation of the analysis result.
     *
     * @return formatted result string
     */
    @Override
    public String toString() {
        return condition + " (" + severity + "): " + message;
    }

    /**
     * Creates a successful analysis result with LOW severity.
     *
     * @param condition the condition assessment
     * @param message the detailed message
     * @return a new AnalysisResult with LOW severity
     */
    public static AnalysisResult success(String condition, String message) {
        return new AnalysisResult(condition, message, "LOW");
    }

    /**
     * Creates a warning analysis result with MEDIUM severity.
     *
     * @param condition the condition assessment
     * @param message the detailed message
     * @return a new AnalysisResult with MEDIUM severity
     */
    public static AnalysisResult warning(String condition, String message) {
        return new AnalysisResult(condition, message, "MEDIUM");
    }

    /**
     * Creates a critical analysis result with HIGH severity.
     *
     * @param condition the condition assessment
     * @param message the detailed message
     * @return a new AnalysisResult with HIGH severity
     */
    public static AnalysisResult critical(String condition, String message) {
        return new AnalysisResult(condition, message, "HIGH");
    }

    /**
     * Creates an emergency analysis result with CRITICAL severity.
     *
     * @param condition the condition assessment
     * @param message the detailed message
     * @return a new AnalysisResult with CRITICAL severity
     */
    public static AnalysisResult emergency(String condition, String message) {
        return new AnalysisResult(condition, message, "CRITICAL");
    }
}