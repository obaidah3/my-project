package com.vehiclediag.ui;

/**
 * Represents a single history row for a diagnostic report.
 */
public class DiagnosticReportHistory {

    private final String timestamp;
    private final String vehicleId;
    private final String type;
    private final String model;
    private final String year;
    private final String faultCode;
    private final String healthScore;
    private final String summary;
    private final String severity;

    public DiagnosticReportHistory(String timestamp,
                                   String vehicleId,
                                   String type,
                                   String model,
                                   String year,
                                   String faultCode,
                                   String healthScore,
                                   String summary,
                                   String severity) {
        this.timestamp = timestamp;
        this.vehicleId = vehicleId;
        this.type = type;
        this.model = model;
        this.year = year;
        this.faultCode = faultCode;
        this.healthScore = healthScore;
        this.summary = summary;
        this.severity = severity;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public String getType() {
        return type;
    }

    public String getModel() {
        return model;
    }

    public String getYear() {
        return year;
    }

    public String getFaultCode() {
        return faultCode;
    }

    public String getHealthScore() {
        return healthScore;
    }

    public String getSummary() {
        return summary;
    }

    public String getSeverity() {
        return severity;
    }
}
