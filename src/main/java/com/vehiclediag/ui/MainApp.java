package com.vehiclediag.ui;

import com.vehiclediag.client.DiagnosticClient;
import com.vehiclediag.model.Car;
import com.vehiclediag.model.DiagnosticReport;
import com.vehiclediag.model.DiagnosticRequest;
import com.vehiclediag.model.Truck;
import com.vehiclediag.model.Vehicle;
import com.vehiclediag.util.Constants;
import com.vehiclediag.util.ValidationUtil;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * JavaFX application for submitting vehicle diagnostic requests to the existing server.
 * This UI layer is separate from the existing console client and reuses DiagnosticClient.
 */
public class MainApp extends Application {

    private TextField vehicleIdField;
    private ComboBox<String> vehicleTypeCombo;
    private TextField modelField;
    private TextField yearField;
    private TextField speedField;
    private TextField rpmField;
    private TextField temperatureField;
    private TextField batteryVoltageField;
    private TextField fuelLevelField;
    private TextField faultCodeField;
    private TextArea resultArea;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Vehicle Diagnostic Dashboard");

        GridPane formGrid = buildFormGrid();
        Button submitButton = new Button("Submit Diagnostic Request");
        submitButton.setOnAction(event -> handleSubmit());

        resultArea = new TextArea();
        resultArea.setEditable(false);
        resultArea.setWrapText(true);
        resultArea.setPromptText("Diagnostic results will appear here...");

        VBox root = new VBox(12, formGrid, submitButton, resultArea);
        root.setPadding(new Insets(16));
        VBox.setVgrow(resultArea, Priority.ALWAYS);

        Scene scene = new Scene(root, 640, 620);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private GridPane buildFormGrid() {
        vehicleIdField = new TextField();
        vehicleTypeCombo = new ComboBox<>();
        vehicleTypeCombo.getItems().addAll("Car", "Truck");
        vehicleTypeCombo.getSelectionModel().selectFirst();
        modelField = new TextField();
        yearField = new TextField();
        speedField = new TextField();
        rpmField = new TextField();
        temperatureField = new TextField();
        batteryVoltageField = new TextField();
        fuelLevelField = new TextField();
        faultCodeField = new TextField();

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        grid.add(new Label("Vehicle ID:"), 0, 0);
        grid.add(vehicleIdField, 1, 0);
        grid.add(new Label("Vehicle Type:"), 0, 1);
        grid.add(vehicleTypeCombo, 1, 1);
        grid.add(new Label("Model:"), 0, 2);
        grid.add(modelField, 1, 2);
        grid.add(new Label("Year:"), 0, 3);
        grid.add(yearField, 1, 3);
        grid.add(new Label("Speed (km/h):"), 0, 4);
        grid.add(speedField, 1, 4);
        grid.add(new Label("RPM:"), 0, 5);
        grid.add(rpmField, 1, 5);
        grid.add(new Label("Engine Temperature (°C):"), 0, 6);
        grid.add(temperatureField, 1, 6);
        grid.add(new Label("Battery Voltage (V):"), 0, 7);
        grid.add(batteryVoltageField, 1, 7);
        grid.add(new Label("Fuel Level (%):"), 0, 8);
        grid.add(fuelLevelField, 1, 8);
        grid.add(new Label("Fault Code:"), 0, 9);
        grid.add(faultCodeField, 1, 9);

        GridPane.setHgrow(vehicleIdField, Priority.ALWAYS);
        GridPane.setHgrow(vehicleTypeCombo, Priority.ALWAYS);
        GridPane.setHgrow(modelField, Priority.ALWAYS);
        GridPane.setHgrow(yearField, Priority.ALWAYS);
        GridPane.setHgrow(speedField, Priority.ALWAYS);
        GridPane.setHgrow(rpmField, Priority.ALWAYS);
        GridPane.setHgrow(temperatureField, Priority.ALWAYS);
        GridPane.setHgrow(batteryVoltageField, Priority.ALWAYS);
        GridPane.setHgrow(fuelLevelField, Priority.ALWAYS);
        GridPane.setHgrow(faultCodeField, Priority.ALWAYS);

        return grid;
    }

    private void handleSubmit() {
        try {
            Vehicle vehicle = buildVehicleFromInput();
            DiagnosticRequest request = new DiagnosticRequest(
                    vehicle,
                    parseDouble(speedField.getText(), "speed"),
                    parseInt(rpmField.getText(), "RPM"),
                    parseDouble(temperatureField.getText(), "engine temperature"),
                    parseDouble(batteryVoltageField.getText(), "battery voltage"),
                    parseDouble(fuelLevelField.getText(), "fuel level"),
                    faultCodeField.getText().trim().isEmpty() ? null : faultCodeField.getText().trim()
            );

            DiagnosticClient diagnosticClient = new DiagnosticClient("localhost", Constants.SERVER_PORT);
            try {
                diagnosticClient.connect();
                Object response = diagnosticClient.sendRequest(request);
                displayResponse(response);
            } finally {
                diagnosticClient.disconnect();
            }
        } catch (Exception e) {
            showError("Submission failed", e.getMessage());
        }
    }

    private Vehicle buildVehicleFromInput() {
        String vehicleId = vehicleIdField.getText().trim();
        String model = modelField.getText().trim();
        int year = parseInt(yearField.getText(), "year");

        ValidationUtil.validateVehicleId(vehicleId);
        ValidationUtil.validateModel(model);
        ValidationUtil.validateYear(year);

        if ("Truck".equals(vehicleTypeCombo.getValue())) {
            return new Truck(vehicleId, model, year);
        }
        return new Car(vehicleId, model, year);
    }

    private void displayResponse(Object response) {
        if (response instanceof DiagnosticReport) {
            resultArea.setText(((DiagnosticReport) response).toString());
        } else if (response instanceof String) {
            resultArea.setText((String) response);
        } else {
            resultArea.setText("Unexpected response type: " + response.getClass().getName());
        }
    }

    private int parseInt(String value, String fieldName) {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid " + fieldName + ". Please enter a whole number.");
        }
    }

    private double parseDouble(String value, String fieldName) {
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid " + fieldName + ". Please enter a numeric value.");
        }
    }

    private void showError(String title, String message) {
        resultArea.setText(title + ": " + message);
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
