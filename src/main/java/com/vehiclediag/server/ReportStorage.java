package com.vehiclediag.server;

import com.vehiclediag.model.DiagnosticReport;
import com.vehiclediag.util.Constants;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;

/**
 * Handles persistent storage of diagnostic reports to CSV format.
 * Thread-safe storage of vehicle diagnostic analysis results.
 */
public class ReportStorage {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportStorage.class);
    private static final String DATA_DIRECTORY = "data";
    private static final String CSV_FILE_PATH = DATA_DIRECTORY + File.separator + Constants.CSV_FILE;
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Lock object for thread-safe file operations
    private final Object fileLock = new Object();

    /**
     * Creates a new ReportStorage instance.
     * Ensures the data directory exists on initialization.
     */
    public ReportStorage() {
        ensureDataDirectoryExists();
    }

    /**
     * Stores a diagnostic report to the CSV file.
     * Creates CSV header if the file is new or empty.
     * Thread-safe append operation.
     *
     * @param report the diagnostic report to store
     * @throws IOException if an I/O error occurs during file operations
     * @throws IllegalArgumentException if the report is null
     */
    public void storeReport(DiagnosticReport report) throws IOException {
        if (report == null) {
            throw new IllegalArgumentException("DiagnosticReport cannot be null");
        }

        try {
            String absolutePath = new File(CSV_FILE_PATH).getAbsolutePath();
            LOGGER.info("Storing diagnostic report to CSV at: {}", absolutePath);
        } catch (Exception e) {
            LOGGER.warn("Failed to log absolute path", e);
        }

        synchronized (fileLock) {
            boolean fileExists = Files.exists(Paths.get(CSV_FILE_PATH));
            boolean fileIsEmpty = fileExists && Files.size(Paths.get(CSV_FILE_PATH)) == 0;

            try (FileWriter fileWriter = new FileWriter(CSV_FILE_PATH, true);
                 CSVPrinter csvPrinter = createCsvPrinter(fileWriter, fileExists && !fileIsEmpty)) {

                // Write header if file is new or empty
                if (!fileExists || fileIsEmpty) {
                    csvPrinter.printRecord((Object[]) Constants.CSV_HEADERS);
                }

                // Write report data
                csvPrinter.printRecord(
                        report.getTimestamp().format(TIMESTAMP_FORMATTER),
                        report.getVehicle().getVehicleId(),
                        report.getVehicle().getVehicleType(),
                        report.getVehicle().getModel(),
                        report.getVehicle().getYear(),
                        String.format("%.1f", report.getSpeed()),
                        report.getRpm(),
                        String.format("%.1f", report.getEngineTemperature()),
                        String.format("%.1f", report.getBatteryVoltage()),
                        String.format("%.1f", report.getFuelLevel()),
                        report.getFaultCode() != null ? report.getFaultCode() : "",
                        String.format("%.1f", report.getOilPressure()),
                        String.format("%.1f", report.getCoolantLevel()),
                        String.format("%.1f", report.getTransmissionTemperature()),
                        String.format("%.1f", report.getThrottlePosition()),
                        String.format("%.1f", report.getMafReading()),
                        String.format("%.2f", report.getOxygenSensorVoltage()),
                        report.getMileage(),
                        report.getEngineCondition(),
                        report.getBatteryCondition(),
                        report.getFuelCondition(),
                        report.getOverallSummary(),
                        report.getHealthScore()
                );

                csvPrinter.flush();
            }
        }
    }

    /**
     * Creates a CSVPrinter with appropriate format settings.
     *
     * @param fileWriter the file writer for CSV output
     * @param skipHeader whether to skip writing the header (file already has one)
     * @return configured CSVPrinter
     * @throws IOException if an I/O error occurs
     */
    private CSVPrinter createCsvPrinter(FileWriter fileWriter, boolean skipHeader) throws IOException {
        CSVFormat csvFormat = CSVFormat.DEFAULT
                .withHeader(Constants.CSV_HEADERS)
                .withSkipHeaderRecord(skipHeader);
        return new CSVPrinter(fileWriter, csvFormat);
    }

    /**
     * Ensures the data directory exists.
     * Creates the directory if it does not exist.
     *
     * @throws RuntimeException if directory creation fails
     */
    private void ensureDataDirectoryExists() {
        File dataDir = new File(DATA_DIRECTORY);
        if (!dataDir.exists()) {
            if (!dataDir.mkdir()) {
                throw new RuntimeException("Failed to create data directory: " + DATA_DIRECTORY);
            }
        }
    }

    /**
     * Returns the path to the CSV file.
     *
     * @return the CSV file path
     */
    public String getFilePath() {
        return CSV_FILE_PATH;
    }

    /**
     * Checks if the CSV file exists.
     *
     * @return true if the file exists, false otherwise
     */
    public boolean fileExists() {
        return Files.exists(Paths.get(CSV_FILE_PATH));
    }

    /**
     * Returns the number of lines in the CSV file (excluding header if present).
     *
     * @return the number of records stored, or -1 if file doesn't exist
     */
    public long getRecordCount() {
        try {
            if (!fileExists()) {
                return 0;
            }
            long lineCount = Files.lines(Paths.get(CSV_FILE_PATH)).count();
            // Subtract 1 if file is not empty (for header row)
            return lineCount > 0 ? lineCount - 1 : 0;
        } catch (IOException e) {
            return -1;
        }
    }
}