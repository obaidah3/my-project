package com.vehiclediag.client;

import com.vehiclediag.model.Car;
import com.vehiclediag.model.DiagnosticReport;
import com.vehiclediag.model.DiagnosticRequest;
import com.vehiclediag.model.Truck;
import com.vehiclediag.model.Vehicle;
import com.vehiclediag.util.Constants;
import com.vehiclediag.util.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Console user interface for the diagnostic client.
 * Guides users through vehicle selection, data entry, and report display.
 */
public class ClientUI {

    private static final Logger logger = LoggerFactory.getLogger(Constants.LOGGER_CLIENT);

    private final Scanner scanner;
    private final DiagnosticClient client;

    /**
     * Creates a new client UI with the specified diagnostic client.
     *
     * @param client the diagnostic client to use for server communication
     */
    public ClientUI(DiagnosticClient client) {
        this.scanner = new Scanner(System.in);
        this.client = client;
    }

    /**
     * Starts the interactive diagnostic client session.
     * Displays menu and handles user interactions until exit.
     */
    public void start() {
        System.out.println("\n=== Vehicle Diagnostic Client ===");
        System.out.println("Server: " + client.getServerHost() + ":" + client.getServerPort());

        // Main menu loop
        boolean running = true;
        while (running) {
            displayMainMenu();
            int choice = getMenuChoice(1, 2);

            switch (choice) {
                case 1:
                    submitDiagnosticRequest();
                    break;
                case 2:
                    running = false;
                    System.out.println("\nGoodbye!");
                    break;
            }
        }
    }

    /**
     * Displays the main menu options.
     */
    private void displayMainMenu() {
        System.out.println("\n--- Main Menu ---");
        System.out.println("1. Submit diagnostic request");
        System.out.println("2. Exit");
        System.out.print("Select option: ");
    }

    /**
     * Gets a validated menu choice from the user.
     *
     * @param minChoice minimum valid choice
     * @param maxChoice maximum valid choice
     * @return the user's choice
     */
    private int getMenuChoice(int minChoice, int maxChoice) {
        while (true) {
            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                if (choice >= minChoice && choice <= maxChoice) {
                    return choice;
                }
                System.out.print("Invalid choice. Please select " + minChoice + "-" + maxChoice + ": ");
            } catch (InputMismatchException e) {
                scanner.nextLine(); // Clear invalid input
                System.out.print("Invalid input. Please enter a number: ");
            }
        }
    }

    /**
     * Guides the user through submitting a diagnostic request.
     */
    private void submitDiagnosticRequest() {
        try {
            // Collect vehicle data
            Vehicle vehicle = collectVehicleData();

            // Collect sensor data
            SensorData sensorData = collectSensorData();

            // Create diagnostic request
            DiagnosticRequest request = new DiagnosticRequest(
                    vehicle,
                    sensorData.speed,
                    sensorData.rpm,
                    sensorData.engineTemperature,
                    sensorData.batteryVoltage,
                    sensorData.fuelLevel,
                    sensorData.faultCode
            );

            // Send to server
            System.out.println("\nSending diagnostic request to server...");
            DiagnosticClient requestClient = new DiagnosticClient(client.getServerHost(), client.getServerPort());
            try {
                requestClient.connect();
                Object response = requestClient.sendRequest(request);

                if (response instanceof DiagnosticReport) {
                    displayDiagnosticReport((DiagnosticReport) response);
                } else if (response instanceof String) {
                    displayErrorResponse((String) response);
                } else {
                    System.out.println("Unexpected response from server: " + response.getClass().getName());
                    logger.warn("Unexpected response type: {}", response.getClass().getName());
                }
            } finally {
                requestClient.disconnect();
            }

        } catch (IllegalArgumentException e) {
            System.out.println("\n✗ Invalid input: " + e.getMessage());
            logger.error("Validation error", e);
        } catch (IOException e) {
            System.out.println("\n✗ Communication error: " + e.getMessage());
            logger.error("I/O error", e);
        } catch (ClassNotFoundException e) {
            System.out.println("\n✗ Protocol error: " + e.getMessage());
            logger.error("Class not found", e);
        }
    }

    /**
     * Collects vehicle information from the user.
     *
     * @return the vehicle object (Car or Truck)
     */
    private Vehicle collectVehicleData() {
        System.out.println("\n--- Vehicle Information ---");

        // Get vehicle ID
        String vehicleId;
        while (true) {
            System.out.print("Vehicle ID: ");
            vehicleId = scanner.nextLine().trim();
            try {
                ValidationUtil.validateVehicleId(vehicleId);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid: " + e.getMessage());
            }
        }

        // Get vehicle type
        System.out.println("\nVehicle Type:");
        System.out.println("1. Car");
        System.out.println("2. Truck");
        System.out.print("Select type: ");
        int vehicleType = getMenuChoice(1, 2);

        // Get model
        String model;
        while (true) {
            System.out.print("Model: ");
            model = scanner.nextLine().trim();
            try {
                ValidationUtil.validateModel(model);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid: " + e.getMessage());
            }
        }

        // Get year
        int year;
        while (true) {
            System.out.print("Year (1900-2100): ");
            try {
                year = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                ValidationUtil.validateYear(year);
                break;
            } catch (InputMismatchException e) {
                scanner.nextLine(); // Clear invalid input
                System.out.println("Invalid: Please enter a valid year");
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid: " + e.getMessage());
            }
        }

        // Create vehicle object
        if (vehicleType == 1) {
            return new Car(vehicleId, model, year);
        } else {
            return new Truck(vehicleId, model, year);
        }
    }

    /**
     * Collects sensor data from the user.
     *
     * @return the sensor data
     */
    private SensorData collectSensorData() {
        System.out.println("\n--- Sensor Readings ---");

        double speed = getDoubleInput("Speed (0-250 km/h): ", 0, Constants.MAX_SPEED_KMH);
        int rpm = getIntInput("RPM (0-8000): ", 0, Constants.MAX_RPM);
        double temperature = getDoubleInput("Engine Temperature (50-120 °C): ", Constants.MIN_TEMP_C, Constants.MAX_TEMP_C);
        double batteryVoltage = getDoubleInput("Battery Voltage (8-16 V): ", Constants.MIN_BATTERY_V, Constants.MAX_BATTERY_V);
        double fuelLevel = getDoubleInput("Fuel Level (0-100 %): ", Constants.MIN_FUEL_LEVEL, Constants.MAX_FUEL_LEVEL);

        System.out.print("Fault Code (or press Enter if none): ");
        String faultCode = scanner.nextLine().trim();
        if (faultCode.isEmpty()) {
            faultCode = null;
        }

        return new SensorData(speed, rpm, temperature, batteryVoltage, fuelLevel, faultCode);
    }

    /**
     * Gets a double input from the user with range validation.
     *
     * @param prompt the input prompt
     * @param min minimum valid value
     * @param max maximum valid value
     * @return the validated double value
     */
    private double getDoubleInput(String prompt, double min, double max) {
        while (true) {
            System.out.print(prompt);
            try {
                double value = scanner.nextDouble();
                scanner.nextLine(); // Consume newline

                if (value >= min && value <= max) {
                    return value;
                }
                System.out.println("Invalid: Value must be between " + min + " and " + max);
            } catch (InputMismatchException e) {
                scanner.nextLine(); // Clear invalid input
                System.out.println("Invalid: Please enter a valid number");
            }
        }
    }

    /**
     * Gets an integer input from the user with range validation.
     *
     * @param prompt the input prompt
     * @param min minimum valid value
     * @param max maximum valid value
     * @return the validated integer value
     */
    private int getIntInput(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                if (value >= min && value <= max) {
                    return value;
                }
                System.out.println("Invalid: Value must be between " + min + " and " + max);
            } catch (InputMismatchException e) {
                scanner.nextLine(); // Clear invalid input
                System.out.println("Invalid: Please enter a valid number");
            }
        }
    }

    /**
     * Displays a diagnostic report to the user.
     *
     * @param report the diagnostic report to display
     */
    private void displayDiagnosticReport(DiagnosticReport report) {
        System.out.println(report.toString());
    }

    /**
     * Displays an error response to the user.
     *
     * @param errorMessage the error message
     */
    private void displayErrorResponse(String errorMessage) {
        System.out.println("\n✗ Server Error: " + errorMessage);
    }

    /**
     * Inner class to hold sensor data.
     */
    private static class SensorData {
        final double speed;
        final int rpm;
        final double engineTemperature;
        final double batteryVoltage;
        final double fuelLevel;
        final String faultCode;

        SensorData(double speed, int rpm, double engineTemperature, double batteryVoltage, double fuelLevel, String faultCode) {
            this.speed = speed;
            this.rpm = rpm;
            this.engineTemperature = engineTemperature;
            this.batteryVoltage = batteryVoltage;
            this.fuelLevel = fuelLevel;
            this.faultCode = faultCode;
        }
    }

    /**
     * Main entry point for the diagnostic client.
     *
     * @param args command line arguments: [server_host] [server_port]
     */
    public static void main(String[] args) {
        String serverHost = "localhost";
        int serverPort = Constants.SERVER_PORT;

        // Parse command line arguments
        if (args.length > 0) {
            serverHost = args[0];
        }
        if (args.length > 1) {
            try {
                serverPort = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid port: " + args[1]);
                System.exit(1);
            }
        }

        DiagnosticClient client = new DiagnosticClient(serverHost, serverPort);
        ClientUI ui = new ClientUI(client);
        ui.start();
    }
}