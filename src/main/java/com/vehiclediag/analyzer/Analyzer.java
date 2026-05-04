package com.vehiclediag.analyzer;

import com.vehiclediag.model.DiagnosticRequest;

/**
 * Interface for all diagnostic analyzers in the vehicle diagnostic system.
 * Each analyzer evaluates specific aspects of vehicle diagnostic data and
 * returns a structured analysis result.
 */
public interface Analyzer {

    /**
     * Analyzes the provided diagnostic request and returns the analysis result.
     * Each analyzer implementation focuses on a specific aspect of vehicle diagnostics
     * (engine, battery, fuel, faults, etc.).
     *
     * @param request the diagnostic request containing vehicle and sensor data
     * @return the analysis result with condition assessment and recommendations
     * @throws IllegalArgumentException if the request is null or contains invalid data
     */
    AnalysisResult analyze(DiagnosticRequest request);
}