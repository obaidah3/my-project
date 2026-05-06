# JavaFX Report History Table Update Fix

## Problem
The diagnostic report was generated and displayed correctly, but the Report History TableView remained empty after submitting a diagnostic request. The CSV file was being written to, but the UI was not refreshing to show the new data.

## Root Causes Identified
1. **Missing Refresh Trigger**: The `handleSubmit()` method was not calling `loadHistory()` after a successful diagnostic submission
2. **Thread Safety**: Updates to JavaFX UI must occur on the JavaFX Application Thread
3. **No Path Verification**: There was no logging to verify that ReportStorage and MainApp were using the same CSV file path

## Solution Implemented

### 1. ReportStorage.java Changes
**Added logging to verify CSV file path:**
```java
// Added import
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Added logger instance
private static final Logger LOGGER = LoggerFactory.getLogger(ReportStorage.class);

// Added to storeReport() method before synchronized block
try {
    String absolutePath = new File(CSV_FILE_PATH).getAbsolutePath();
    LOGGER.info("Storing diagnostic report to CSV at: {}", absolutePath);
} catch (Exception e) {
    LOGGER.warn("Failed to log absolute path", e);
}
```

**Why**: When a diagnostic report is stored, the absolute file path is logged. This helps verify that ReportStorage is using the correct path (data/DiagnosticReports.csv resolved to absolute path).

### 2. MainApp.java Changes

#### 2a. Added Logger for Path Verification
```java
// Added import
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Added logger instance
private static final Logger LOGGER = LoggerFactory.getLogger(MainApp.class);
```

#### 2b. Modified handleSubmit() to Auto-Refresh History
```java
DiagnosticClient diagnosticClient = new DiagnosticClient("localhost", Constants.SERVER_PORT);
try {
    diagnosticClient.connect();
    Object response = diagnosticClient.sendRequest(request);
    displayResponse(response);
    
    // Refresh history table after successful submission
    Platform.runLater(() -> {
        LOGGER.info("Refreshing history table after successful diagnostic submission");
        loadHistory();
    });
} finally {
    diagnosticClient.disconnect();
}
```

**Why**: 
- After a successful diagnostic submission, the code now explicitly calls `loadHistory()` to refresh the TableView
- Uses `Platform.runLater()` to ensure the UI update happens on the JavaFX Application Thread (thread-safe)
- Logs that the refresh is happening for debugging purposes

#### 2c. Enhanced readHistoryFromCsv() with Logging
```java
private java.util.List<DiagnosticReportHistory> readHistoryFromCsv() throws java.io.IOException {
    java.nio.file.Path csvPath = java.nio.file.Paths.get("data", com.vehiclediag.util.Constants.CSV_FILE);
    
    try {
        String absolutePath = csvPath.toAbsolutePath().toString();
        LOGGER.info("Loading diagnostic history from CSV at: {}", absolutePath);
    } catch (Exception e) {
        LOGGER.warn("Failed to log absolute path", e);
    }
    
    java.util.List<DiagnosticReportHistory> history = new java.util.ArrayList<>();

    if (!java.nio.file.Files.exists(csvPath)) {
        LOGGER.debug("CSV file does not exist at: {}, returning empty history", csvPath);
        return history;
    }
    // ... rest of method
}
```

**Why**:
- Logs the absolute path when loading history for verification
- Gracefully handles missing CSV file and logs it at DEBUG level
- Allows comparison of paths logged by ReportStorage and MainApp to verify they match

## How to Verify the Fix

### Run the Application
1. Start the server:
   ```bash
   java -cp target/classes:... com.vehiclediag.server.DiagnosticServer 5000
   ```

2. Run the JavaFX client:
   ```bash
   java -cp target/classes:... com.vehiclediag.ui.MainApp
   ```

### Check the Logs
After submitting a diagnostic request, check the application logs for these messages:

1. **From ReportStorage (server-side)**:
   ```
   [INFO] Storing diagnostic report to CSV at: /absolute/path/to/project/data/DiagnosticReports.csv
   ```

2. **From MainApp (client-side)**:
   ```
   [INFO] Refreshing history table after successful diagnostic submission
   [INFO] Loading diagnostic history from CSV at: /absolute/path/to/project/data/DiagnosticReports.csv
   ```

The paths should match, confirming both components are using the same CSV file.

### Expected Behavior
1. Submit a diagnostic request with all required fields
2. The diagnostic result displays in the "Diagnostic Results" text area
3. **The Report History TableView automatically populates with the new record** (this is the fix)
4. The new record appears with all columns: Timestamp, Vehicle ID, Type, Model, Year, Fault Code, Health Score, Summary, Severity

## Requirements Met

✅ **Auto-refresh after submission**: Added `Platform.runLater(() -> loadHistory())` in `handleSubmit()`

✅ **CSV path logging**: Both ReportStorage and MainApp log their absolute CSV paths

✅ **Graceful missing file handling**: Returns empty list without error if CSV doesn't exist

✅ **No server/model/analyzer changes**: Only modified MainApp and ReportStorage UI/logging

✅ **No data loss**: Existing CSV data is preserved; only appending new records

## Files Modified
1. `src/main/java/com/vehiclediag/server/ReportStorage.java` - Added logging
2. `src/main/java/com/vehiclediag/ui/MainApp.java` - Added logger and auto-refresh logic

## Compilation Status
✅ **BUILD SUCCESS** - All changes compile without errors or warnings
