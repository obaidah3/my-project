package com.vehiclediag.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class providing convenient logging methods for the Vehicle Diagnostic System.
 * Wraps SLF4J Logger with helper methods for common logging patterns and consistent formatting.
 *
 * @author Vehicle Diagnostic System
 * @version 1.0.0
 */
public final class LoggerUtil {

    /**
     * Gets a logger instance for the specified class.
     *
     * @param clazz the class for which to get the logger
     * @return SLF4J logger instance
     */
    public static Logger getLogger(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }

    /**
     * Gets a logger instance for the specified name.
     *
     * @param name the logger name
     * @return SLF4J logger instance
     */
    public static Logger getLogger(String name) {
        return LoggerFactory.getLogger(name);
    }

    /**
     * Logs server startup information.
     *
     * @param logger the logger to use
     * @param port the server port
     */
    public static void logServerStartup(Logger logger, int port) {
        logger.info("=== Vehicle Diagnostic Server Starting ===");
        logger.info("Server port: {}", port);
        logger.info("Max concurrent clients: {}", Constants.MAX_CLIENTS);
        logger.info("CSV file: {}", Constants.CSV_FILE);
        logger.info("Response timeout: {}ms", Constants.RESPONSE_TIMEOUT_MS);
        logger.info("==========================================");
    }

    /**
     * Logs server shutdown information.
     *
     * @param logger the logger to use
     */
    public static void logServerShutdown(Logger logger) {
        logger.info("=== Vehicle Diagnostic Server Shutting Down ===");
        logger.info("Closing all connections and cleaning up resources");
        logger.info("===============================================");
    }

    /**
     * Logs client connection acceptance.
     *
     * @param logger the logger to use
     * @param clientId unique client identifier
     * @param clientAddress client IP address
     * @param threadName name of the handling thread
     */
    public static void logClientConnection(Logger logger, String clientId, String clientAddress, String threadName) {
        logger.info("Client connected - ID: {}, Address: {}, Thread: {}", clientId, clientAddress, threadName);
    }

    /**
     * Logs client disconnection.
     *
     * @param logger the logger to use
     * @param clientId unique client identifier
     * @param reason disconnection reason
     */
    public static void logClientDisconnection(Logger logger, String clientId, String reason) {
        logger.info("Client disconnected - ID: {}, Reason: {}", clientId, reason);
    }

    /**
     * Logs diagnostic request processing start.
     *
     * @param logger the logger to use
     * @param clientId unique client identifier
     * @param vehicleId vehicle identifier from request
     */
    public static void logRequestProcessingStart(Logger logger, String clientId, String vehicleId) {
        logger.debug("Processing diagnostic request - Client: {}, Vehicle: {}", clientId, vehicleId);
    }

    /**
     * Logs diagnostic request processing completion.
     *
     * @param logger the logger to use
     * @param clientId unique client identifier
     * @param vehicleId vehicle identifier from request
     * @param processingTimeMs time taken to process in milliseconds
     */
    public static void logRequestProcessingComplete(Logger logger, String clientId, String vehicleId, long processingTimeMs) {
        logger.info("Diagnostic request completed - Client: {}, Vehicle: {}, Time: {}ms",
                   clientId, vehicleId, processingTimeMs);
    }

    /**
     * Logs CSV report persistence.
     *
     * @param logger the logger to use
     * @param vehicleId vehicle identifier
     * @param recordCount total records in CSV after append
     */
    public static void logCsvPersistence(Logger logger, String vehicleId, int recordCount) {
        logger.debug("Diagnostic report persisted to CSV - Vehicle: {}, Total records: {}", vehicleId, recordCount);
    }

    /**
     * Logs analyzer execution.
     *
     * @param logger the logger to use
     * @param analyzerName name of the analyzer
     * @param vehicleId vehicle identifier
     * @param result analysis result
     */
    public static void logAnalyzerExecution(Logger logger, String analyzerName, String vehicleId, String result) {
        logger.debug("Analyzer executed - Name: {}, Vehicle: {}, Result: {}", analyzerName, vehicleId, result);
    }

    /**
     * Logs client startup information.
     *
     * @param logger the logger to use
     * @param host server hostname
     * @param port server port
     */
    public static void logClientStartup(Logger logger, String host, int port) {
        logger.info("=== Vehicle Diagnostic Client Starting ===");
        logger.info("Server: {}:{}", host, port);
        logger.info("Response timeout: {}ms", Constants.RESPONSE_TIMEOUT_MS);
        logger.info("========================================");
    }

    /**
     * Logs client shutdown information.
     *
     * @param logger the logger to use
     */
    public static void logClientShutdown(Logger logger) {
        logger.info("=== Vehicle Diagnostic Client Shutting Down ===");
        logger.info("Closing connection and cleaning up resources");
        logger.info("==============================================");
    }

    /**
     * Logs validation errors with context.
     *
     * @param logger the logger to use
     * @param context validation context (e.g., "vehicle ID", "speed")
     * @param value the invalid value
     * @param errorMessage detailed error message
     */
    public static void logValidationError(Logger logger, String context, Object value, String errorMessage) {
        logger.warn("Validation failed - Context: {}, Value: {}, Error: {}", context, value, errorMessage);
    }

    /**
     * Logs socket communication errors.
     *
     * @param logger the logger to use
     * @param operation operation that failed (e.g., "connect", "send", "receive")
     * @param clientId client identifier (if available)
     * @param exception the exception that occurred
     */
    public static void logSocketError(Logger logger, String operation, String clientId, Exception exception) {
        logger.error("Socket communication error - Operation: {}, Client: {}, Error: {}",
                    operation, clientId != null ? clientId : "unknown", exception.getMessage());
    }

    /**
     * Logs serialization/deserialization errors.
     *
     * @param logger the logger to use
     * @param operation operation that failed (e.g., "serialize", "deserialize")
     * @param objectType type of object being serialized/deserialized
     * @param exception the exception that occurred
     */
    public static void logSerializationError(Logger logger, String operation, String objectType, Exception exception) {
        logger.error("Serialization error - Operation: {}, Type: {}, Error: {}",
                    operation, objectType, exception.getMessage());
    }

    /**
     * Logs thread pool status.
     *
     * @param logger the logger to use
     * @param activeThreads number of active threads
     * @param poolSize current pool size
     * @param queueSize number of tasks in queue
     */
    public static void logThreadPoolStatus(Logger logger, int activeThreads, int poolSize, int queueSize) {
        logger.debug("Thread pool status - Active: {}, Pool size: {}, Queue: {}", activeThreads, poolSize, queueSize);
    }

    /**
     * Logs performance metrics.
     *
     * @param logger the logger to use
     * @param operation operation being measured
     * @param durationMs duration in milliseconds
     * @param success whether the operation succeeded
     */
    public static void logPerformance(Logger logger, String operation, long durationMs, boolean success) {
        if (success) {
            logger.debug("Performance - Operation: {}, Duration: {}ms", operation, durationMs);
        } else {
            logger.warn("Performance - Operation: {} failed after {}ms", operation, durationMs);
        }
    }

    /**
     * Logs system health status.
     *
     * @param logger the logger to use
     * @param component component being checked
     * @param status health status
     * @param details additional status details
     */
    public static void logHealthStatus(Logger logger, String component, String status, String details) {
        logger.info("Health check - Component: {}, Status: {}, Details: {}", component, status, details);
    }

    /**
     * Logs CSV file operations.
     *
     * @param logger the logger to use
     * @param operation operation performed (e.g., "create", "append", "read")
     * @param fileName CSV file name
     * @param recordCount number of records affected
     */
    public static void logCsvOperation(Logger logger, String operation, String fileName, int recordCount) {
        logger.debug("CSV operation - Type: {}, File: {}, Records: {}", operation, fileName, recordCount);
    }

    /**
     * Logs configuration loading.
     *
     * @param logger the logger to use
     * @param configType type of configuration loaded
     * @param source configuration source
     */
    public static void logConfigLoad(Logger logger, String configType, String source) {
        logger.info("Configuration loaded - Type: {}, Source: {}", configType, source);
    }

    // Utility class - prevent instantiation
    private LoggerUtil() {
        throw new UnsupportedOperationException("LoggerUtil class cannot be instantiated");
    }
}