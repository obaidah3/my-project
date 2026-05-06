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
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Professional JavaFX Dashboard for Vehicle Diagnostic System.
 * Features modern card-based layout with analytics and real-time updates.
 */
public class MainApp extends Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainApp.class);

    // Form fields
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
    private TextField oilPressureField;
    private TextField coolantLevelField;
    private TextField transmissionTemperatureField;
    private TextField throttlePositionField;
    private TextField mafReadingField;
    private TextField oxygenSensorVoltageField;
    private TextField mileageField;

    // Result display
    private TextArea resultArea;
    private TextArea recommendationsArea;
    private Label resultSummaryLabel;
    private Label healthScoreLabel;
    private Label severityBadgeLabel;
    private Label statusLabel;

    // History
    private TextField historySearchField;
    private ComboBox<String> severityFilterCombo;
    private TableView<DiagnosticReportHistory> historyTable;
    private ObservableList<DiagnosticReportHistory> historyData;
    private FilteredList<DiagnosticReportHistory> filteredHistory;

    // Analytics
    private PieChart severityChart;
    private BarChart<String, Number> healthScoreChart;
    private Label totalReportsLabel;
    private Label criticalReportsLabel;
    private Label avgHealthScoreLabel;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Smart Vehicle Diagnostic Dashboard");

        BorderPane root = new BorderPane();
        root.getStyleClass().add("dashboard-root");

        // Build sections
        root.setTop(buildTitleBar());
        root.setCenter(buildMainPane());
        root.setBottom(buildBottomPanel());

        // Load stylesheet
        String css = getClass().getResource("/styles/dashboard.css").toExternalForm();
        Scene scene = new Scene(root, 1280, 760);
        scene.getStylesheets().add(css);

        primaryStage.setScene(scene);
        primaryStage.setMinWidth(1100);
        primaryStage.setMinHeight(650);
        primaryStage.centerOnScreen();
        primaryStage.show();

        // Initial data load
        loadHistory();
    }

    private HBox buildTitleBar() {
        HBox titleBar = new HBox();
        titleBar.getStyleClass().add("title-bar");
        titleBar.setAlignment(Pos.CENTER_LEFT);
        titleBar.setSpacing(16);
        titleBar.setPrefHeight(70);
        titleBar.setMaxHeight(70);

        Label titleLabel = new Label("Smart Vehicle Diagnostic Dashboard");
        titleLabel.getStyleClass().add("title-label");

        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label serverStatusLabel = new Label("Server: Ready");
        serverStatusLabel.getStyleClass().add("server-status");

        titleBar.getChildren().addAll(titleLabel, spacer, serverStatusLabel);
        return titleBar;
    }

    private SplitPane buildMainPane() {
        ScrollPane leftPane = buildLeftPanel();
        leftPane.setPrefWidth(340);
        leftPane.setMinWidth(320);
        leftPane.setMaxWidth(360);

        VBox centerPane = buildCenterPanel();
        centerPane.setPrefWidth(450);
        centerPane.setMinWidth(400);

        VBox rightPane = buildRightPanel();
        rightPane.setPrefWidth(360);
        rightPane.setMinWidth(340);
        rightPane.setMaxWidth(380);

        SplitPane splitPane = new SplitPane(leftPane, centerPane, rightPane);
        splitPane.setDividerPositions(0.27, 0.65);
        splitPane.setStyle("-fx-background-color: transparent;");
        return splitPane;
    }

    private ScrollPane buildLeftPanel() {
        VBox content = new VBox(14);
        content.getStyleClass().add("side-panel");
        content.setPadding(new Insets(16));

        content.getChildren().addAll(
                buildVehicleCard(),
                buildBasicSensorsCard(),
                buildAdvancedSensorsCard(),
                buildSubmitPanel()
        );

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background: transparent; -fx-padding: 0;");
        return scrollPane;
    }

    private Pane buildVehicleCard() {
        VBox card = createCard("Vehicle Info");
        GridPane grid = createFormGrid();

        vehicleIdField = createFormField();
        vehicleTypeCombo = new ComboBox<>();
        vehicleTypeCombo.getItems().addAll("Car", "Truck");
        vehicleTypeCombo.getSelectionModel().selectFirst();
        vehicleTypeCombo.getStyleClass().add("combo-box");
        modelField = createFormField();
        yearField = createFormField();

        grid.add(createLabel("Vehicle ID:"), 0, 0);
        grid.add(vehicleIdField, 1, 0);
        grid.add(createLabel("Type:"), 0, 1);
        grid.add(vehicleTypeCombo, 1, 1);
        grid.add(createLabel("Model:"), 0, 2);
        grid.add(modelField, 1, 2);
        grid.add(createLabel("Year:"), 0, 3);
        grid.add(yearField, 1, 3);

        card.getChildren().add(grid);
        return card;
    }

    private Pane buildBasicSensorsCard() {
        VBox card = createCard("Basic Sensors");
        GridPane grid = createFormGrid();

        speedField = createFormField();
        rpmField = createFormField();
        temperatureField = createFormField();
        batteryVoltageField = createFormField();
        fuelLevelField = createFormField();

        grid.add(createLabel("Speed (km/h):"), 0, 0);
        grid.add(speedField, 1, 0);
        grid.add(createLabel("RPM:"), 0, 1);
        grid.add(rpmField, 1, 1);
        grid.add(createLabel("Temp (°C):"), 0, 2);
        grid.add(temperatureField, 1, 2);
        grid.add(createLabel("Battery (V):"), 0, 3);
        grid.add(batteryVoltageField, 1, 3);
        grid.add(createLabel("Fuel (%):"), 0, 4);
        grid.add(fuelLevelField, 1, 4);

        card.getChildren().add(grid);
        return card;
    }

    private Pane buildAdvancedSensorsCard() {
        VBox card = createCard("Advanced Sensors");
        GridPane grid = createFormGrid();

        oilPressureField = createFormField();
        coolantLevelField = createFormField();
        transmissionTemperatureField = createFormField();
        throttlePositionField = createFormField();
        mafReadingField = createFormField();
        oxygenSensorVoltageField = createFormField();
        mileageField = createFormField();
        faultCodeField = createFormField();

        grid.add(createLabel("Oil Pressure:"), 0, 0);
        grid.add(oilPressureField, 1, 0);
        grid.add(createLabel("Coolant (%):"), 0, 1);
        grid.add(coolantLevelField, 1, 1);
        grid.add(createLabel("Trans Temp (°C):"), 0, 2);
        grid.add(transmissionTemperatureField, 1, 2);
        grid.add(createLabel("Throttle (%):"), 0, 3);
        grid.add(throttlePositionField, 1, 3);
        grid.add(createLabel("MAF:"), 0, 4);
        grid.add(mafReadingField, 1, 4);
        grid.add(createLabel("O2 Sensor (V):"), 0, 5);
        grid.add(oxygenSensorVoltageField, 1, 5);
        grid.add(createLabel("Mileage (km):"), 0, 6);
        grid.add(mileageField, 1, 6);
        grid.add(createLabel("Fault Code:"), 0, 7);
        grid.add(faultCodeField, 1, 7);

        card.getChildren().add(grid);
        return card;
    }



    private Pane buildSubmitPanel() {
        HBox submitContainer = new HBox();
        submitContainer.setAlignment(Pos.CENTER);

        Button submitButton = new Button("Submit Diagnostic");
        submitButton.getStyleClass().add("dashboard-button");
        submitButton.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(submitButton, Priority.ALWAYS);
        submitButton.setOnAction(event -> handleSubmit());

        submitContainer.getChildren().add(submitButton);
        return submitContainer;
    }

    private VBox buildCenterPanel() {
        VBox centerPanel = new VBox(14);
        centerPanel.getStyleClass().add("center-panel");
        centerPanel.setPadding(new Insets(16));

        // Compact result summary
        HBox summaryRow = new HBox(16);
        summaryRow.setAlignment(Pos.CENTER_LEFT);

        resultSummaryLabel = new Label("No diagnostic data yet");
        resultSummaryLabel.getStyleClass().add("result-summary");

        HBox badges = new HBox(8);
        healthScoreLabel = new Label("Health: --");
        healthScoreLabel.getStyleClass().add("health-badge");
        severityBadgeLabel = new Label("--");
        severityBadgeLabel.getStyleClass().add("severity-badge");
        badges.getChildren().addAll(healthScoreLabel, severityBadgeLabel);
        badges.setAlignment(Pos.CENTER_RIGHT);

        summaryRow.getChildren().addAll(resultSummaryLabel, badges);
        HBox.setHgrow(resultSummaryLabel, Priority.ALWAYS);

        // Fixed height details area
        resultArea = new TextArea();
        resultArea.setEditable(false);
        resultArea.setWrapText(true);
        resultArea.getStyleClass().add("result-details");
        resultArea.setPromptText("Diagnostic details and recommendations will appear here...");
        resultArea.setPrefHeight(200);
        resultArea.setMaxHeight(200);

        // Fixed height recommendations area
        recommendationsArea = new TextArea();
        recommendationsArea.setEditable(false);
        recommendationsArea.setWrapText(true);
        recommendationsArea.getStyleClass().add("result-recommendations");
        recommendationsArea.setPromptText("Recommendations will appear here...");
        recommendationsArea.setPrefHeight(150);
        recommendationsArea.setMaxHeight(150);

        centerPanel.getChildren().addAll(summaryRow, resultArea, recommendationsArea);
        return centerPanel;
    }

    private VBox buildRightPanel() {
        VBox analyticsPanel = new VBox(14);
        analyticsPanel.getStyleClass().add("analytics-panel");
        analyticsPanel.setPadding(new Insets(16));

        // Compact stats grid
        GridPane statsGrid = new GridPane();
        statsGrid.setHgap(16);
        statsGrid.setVgap(8);
        statsGrid.getStyleClass().add("stats-grid");

        totalReportsLabel = new Label("0");
        totalReportsLabel.getStyleClass().add("stat-value");
        criticalReportsLabel = new Label("0");
        criticalReportsLabel.getStyleClass().add("stat-value");
        avgHealthScoreLabel = new Label("--");
        avgHealthScoreLabel.getStyleClass().add("stat-value");

        statsGrid.add(new Label("Total:"), 0, 0);
        statsGrid.add(totalReportsLabel, 1, 0);
        statsGrid.add(new Label("Critical:"), 0, 1);
        statsGrid.add(criticalReportsLabel, 1, 1);
        statsGrid.add(new Label("Avg Health:"), 0, 2);
        statsGrid.add(avgHealthScoreLabel, 1, 2);

        // Fixed height charts
        severityChart = new PieChart();
        severityChart.setLegendVisible(false);
        severityChart.setLabelsVisible(true);
        severityChart.setPrefHeight(220);
        severityChart.setMaxHeight(220);
        severityChart.getStyleClass().add("analytics-chart");

        healthScoreChart = createHealthScoreChart();
        healthScoreChart.setPrefHeight(220);
        healthScoreChart.setMaxHeight(220);
        healthScoreChart.getStyleClass().add("analytics-chart");

        analyticsPanel.getChildren().addAll(statsGrid, severityChart, healthScoreChart);
        return analyticsPanel;
    }

    private Pane buildStatsCard() {
        VBox card = new VBox(10);
        card.setStyle("-fx-border-radius: 8; -fx-background-radius: 8; -fx-background-color: white; " +
                "-fx-padding: 16px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 4, 0, 0, 2);");

        Label cardTitle = new Label("Analytics Summary");
        cardTitle.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #1b5e20;");

        Separator sep = new Separator();
        sep.setStyle("-fx-border-color: #e0e0e0;");

        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(12);

        // Total Reports
        Label totalLabel = new Label("Total Reports");
        totalLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #555555;");
        totalReportsLabel = new Label("0");
        totalReportsLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #1b5e20;");

        // Critical Reports
        Label criticalLabel = new Label("Critical Reports");
        criticalLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #555555;");
        criticalReportsLabel = new Label("0");
        criticalReportsLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #d32f2f;");

        // Avg Health Score
        Label avgLabel = new Label("Avg Health Score");
        avgLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #555555;");
        avgHealthScoreLabel = new Label("--");
        avgHealthScoreLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #7cb342;");

        grid.add(totalLabel, 0, 0);
        grid.add(totalReportsLabel, 0, 1);
        grid.add(criticalLabel, 1, 0);
        grid.add(criticalReportsLabel, 1, 1);
        grid.add(avgLabel, 0, 2);
        grid.add(avgHealthScoreLabel, 0, 3);

        card.getChildren().addAll(cardTitle, sep, grid);
        return card;
    }

    private BarChart<String, Number> createHealthScoreChart() {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Vehicle ID");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Avg Health Score");
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Avg Health Score by Vehicle");
        barChart.setStyle("-fx-font-size: 10px;");
        return barChart;
    }

    private VBox buildBottomPanel() {
        VBox historyPanel = new VBox(10);
        historyPanel.getStyleClass().add("history-panel");
        historyPanel.setPadding(new Insets(16));
        historyPanel.setPrefHeight(240);
        historyPanel.setMaxHeight(240);

        // Filter bar with search and severity filter
        HBox filterBar = new HBox(12);
        filterBar.getStyleClass().add("history-filter-bar");
        filterBar.setAlignment(Pos.CENTER_LEFT);

        Label searchLabel = new Label("Search:");
        searchLabel.getStyleClass().add("form-label");
        historySearchField = new TextField();
        historySearchField.setPromptText("Vehicle ID...");
        historySearchField.getStyleClass().add("form-field-small");
        historySearchField.setPrefWidth(150);

        Label severityLabel = new Label("Severity:");
        severityLabel.getStyleClass().add("form-label");
        severityFilterCombo = new ComboBox<>();
        severityFilterCombo.getItems().addAll("All", "CRITICAL", "HIGH", "MEDIUM", "LOW");
        severityFilterCombo.getSelectionModel().selectFirst();
        severityFilterCombo.getStyleClass().add("combo-box");
        severityFilterCombo.setPrefWidth(100);

        Button refreshButton = new Button("Refresh");
        refreshButton.getStyleClass().add("secondary-button");
        refreshButton.setOnAction(event -> loadHistory());

        filterBar.getChildren().addAll(searchLabel, historySearchField, severityLabel, severityFilterCombo, refreshButton);

        // Status label
        statusLabel = new Label("Loading history...");
        statusLabel.getStyleClass().add("status-label");

        // History table with CONSTRAINED_RESIZE_POLICY
        historyTable = new TableView<>();
        historyTable.getStyleClass().add("history-table");
        historyTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        historyTable.setPlaceholder(new Label("No diagnostic history available"));
        historyTable.setPrefHeight(160);

        TableColumn<DiagnosticReportHistory, String> timestampCol = new TableColumn<>("Timestamp");
        timestampCol.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        TableColumn<DiagnosticReportHistory, String> vehicleIdCol = new TableColumn<>("Vehicle ID");
        vehicleIdCol.setCellValueFactory(new PropertyValueFactory<>("vehicleId"));
        TableColumn<DiagnosticReportHistory, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        TableColumn<DiagnosticReportHistory, String> modelCol = new TableColumn<>("Model");
        modelCol.setCellValueFactory(new PropertyValueFactory<>("model"));
        TableColumn<DiagnosticReportHistory, String> yearCol = new TableColumn<>("Year");
        yearCol.setCellValueFactory(new PropertyValueFactory<>("year"));
        TableColumn<DiagnosticReportHistory, String> faultCodeCol = new TableColumn<>("Fault Code");
        faultCodeCol.setCellValueFactory(new PropertyValueFactory<>("faultCode"));
        TableColumn<DiagnosticReportHistory, String> healthScoreCol = new TableColumn<>("Health Score");
        healthScoreCol.setCellValueFactory(new PropertyValueFactory<>("healthScore"));
        TableColumn<DiagnosticReportHistory, String> summaryCol = new TableColumn<>("Summary");
        summaryCol.setCellValueFactory(new PropertyValueFactory<>("summary"));
        TableColumn<DiagnosticReportHistory, String> severityCol = new TableColumn<>("Severity");
        severityCol.setCellValueFactory(new PropertyValueFactory<>("severity"));

        historyTable.getColumns().addAll(timestampCol, vehicleIdCol, typeCol, modelCol, yearCol,
                faultCodeCol, healthScoreCol, summaryCol, severityCol);

        historyData = FXCollections.observableArrayList();
        filteredHistory = new FilteredList<>(historyData, report -> true);
        SortedList<DiagnosticReportHistory> sortedHistory = new SortedList<>(filteredHistory);
        sortedHistory.comparatorProperty().bind(historyTable.comparatorProperty());
        historyTable.setItems(sortedHistory);

        historySearchField.textProperty().addListener((observable, oldValue, newValue) -> applyHistoryFilter());
        severityFilterCombo.valueProperty().addListener((observable, oldValue, newValue) -> applyHistoryFilter());

        historyPanel.getChildren().addAll(filterBar, statusLabel, historyTable);
        return historyPanel;
    }

    private TextField createFormField() {
        TextField field = new TextField();
        field.getStyleClass().add("form-field");
        return field;
    }

    private Label createLabel(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("form-label");
        return label;
    }

    private VBox createCard(String title) {
        VBox card = new VBox(12);
        card.getStyleClass().add("dashboard-card");

        Label cardTitle = new Label(title);
        cardTitle.getStyleClass().add("card-title");
        Separator sep = new Separator();
        sep.getStyleClass().add("card-separator");

        card.getChildren().addAll(cardTitle, sep);
        return card;
    }

    private GridPane createFormGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(10);
        grid.getColumnConstraints().add(new ColumnConstraints(130, 130, 130)); // Fixed label width
        grid.getColumnConstraints().add(new ColumnConstraints(0, 100, Double.MAX_VALUE, Priority.ALWAYS, HPos.LEFT, true)); // Growing input width
        return grid;
    }



    private void fillGridColumns(GridPane grid, int rowCount) {
        for (int i = 0; i < rowCount; i++) {
            GridPane.setHgrow(grid.getChildren().get(i * 2 + 1), Priority.ALWAYS);
        }
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
                    faultCodeField.getText().trim().isEmpty() ? null : faultCodeField.getText().trim(),
                    parseDouble(oilPressureField.getText(), "oil pressure"),
                    parseDouble(coolantLevelField.getText(), "coolant level"),
                    parseDouble(transmissionTemperatureField.getText(), "transmission temperature"),
                    parseDouble(throttlePositionField.getText(), "throttle position"),
                    parseDouble(mafReadingField.getText(), "MAF reading"),
                    parseDouble(oxygenSensorVoltageField.getText(), "oxygen sensor voltage"),
                    parseInt(mileageField.getText(), "mileage")
            );

            DiagnosticClient diagnosticClient = new DiagnosticClient("localhost", Constants.SERVER_PORT);
            try {
                diagnosticClient.connect();
                Object response = diagnosticClient.sendRequest(request);
                displayResponse(response);

                // Refresh history and analytics
                Platform.runLater(() -> {
                    LOGGER.info("Refreshing history and analytics after successful diagnostic submission");
                    loadHistory();
                    updateAnalytics();
                });
            } finally {
                diagnosticClient.disconnect();
            }
        } catch (Exception e) {
            showError("Submission failed", e.getMessage());
        }
    }

    private void displayResponse(Object response) {
        if (response instanceof DiagnosticReport) {
            DiagnosticReport report = (DiagnosticReport) response;
            resultArea.setText(report.toString());
            resultSummaryLabel.setText("Summary: " + report.getOverallSummary());
            healthScoreLabel.setText("Health Score: " + report.getHealthScore());
            updateSeverityBadge(report.getOverallSummary());
        } else {
            resultArea.setText("Unexpected response: " + response);
        }
    }

    private void updateSeverityBadge(String summary) {
        if (summary == null) {
            severityBadgeLabel.setText("--");
            severityBadgeLabel.setStyle("-fx-padding: 4px 8px; -fx-border-radius: 4; -fx-font-weight: bold; -fx-font-size: 11px; -fx-background-color: #cccccc;");
            return;
        }

        String normalized = summary.trim().toUpperCase();
        if (normalized.startsWith("CRITICAL")) {
            severityBadgeLabel.setText("CRITICAL");
            severityBadgeLabel.setStyle("-fx-padding: 4px 8px; -fx-border-radius: 4; -fx-font-weight: bold; -fx-font-size: 11px; " +
                    "-fx-background-color: #d32f2f; -fx-text-fill: white;");
        } else if (normalized.startsWith("WARNING")) {
            severityBadgeLabel.setText("HIGH");
            severityBadgeLabel.setStyle("-fx-padding: 4px 8px; -fx-border-radius: 4; -fx-font-weight: bold; -fx-font-size: 11px; " +
                    "-fx-background-color: #f57c00; -fx-text-fill: white;");
        } else if (normalized.startsWith("MODERATE")) {
            severityBadgeLabel.setText("MEDIUM");
            severityBadgeLabel.setStyle("-fx-padding: 4px 8px; -fx-border-radius: 4; -fx-font-weight: bold; -fx-font-size: 11px; " +
                    "-fx-background-color: #fbc02d; -fx-text-fill: #333333;");
        } else {
            severityBadgeLabel.setText("LOW");
            severityBadgeLabel.setStyle("-fx-padding: 4px 8px; -fx-border-radius: 4; -fx-font-weight: bold; -fx-font-size: 11px; " +
                    "-fx-background-color: #7cb342; -fx-text-fill: white;");
        }
    }

    private void loadHistory() {
        try {
            java.util.List<DiagnosticReportHistory> records = readHistoryFromCsv();
            LOGGER.info("CSV read completed: {} records parsed", records.size());
            System.out.println("CSV read completed: " + records.size() + " records parsed");

            if (!records.isEmpty()) {
                DiagnosticReportHistory firstRecord = records.get(0);
                LOGGER.info("First record - timestamp: {}, vehicleId: {}, severity: {}",
                        firstRecord.getTimestamp(), firstRecord.getVehicleId(), firstRecord.getSeverity());
                System.out.println("First record - timestamp: " + firstRecord.getTimestamp() +
                        ", vehicleId: " + firstRecord.getVehicleId() + ", severity: " + firstRecord.getSeverity());
            }

            historyData = FXCollections.observableArrayList(records);
            filteredHistory = new FilteredList<>(historyData, report -> true);
            SortedList<DiagnosticReportHistory> sortedHistory = new SortedList<>(filteredHistory);
            sortedHistory.comparatorProperty().bind(historyTable.comparatorProperty());
            historyTable.setItems(sortedHistory);

            LOGGER.info("TableView items set with {} records", historyData.size());
            System.out.println("TableView items set with " + historyData.size() + " records");
            System.out.println("TableView.getItems().size() = " + historyTable.getItems().size());

            statusLabel.setText("Loaded " + records.size() + " reports from CSV");

            applyHistoryFilter();
            updateAnalytics();
        } catch (Exception e) {
            LOGGER.error("Failed to load history", e);
            System.out.println("ERROR: Failed to load history: " + e.getMessage());
            statusLabel.setText("Error loading history: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void updateAnalytics() {
        Map<String, Integer> severityCount = new HashMap<>();
        Map<String, Integer> vehicleReports = new HashMap<>();
        Map<String, Double> vehicleHealthScores = new HashMap<>();
        int totalCritical = 0;
        double totalHealth = 0;

        for (DiagnosticReportHistory report : historyData) {
            // Severity distribution
            String severity = report.getSeverity() != null ? report.getSeverity() : "LOW";
            severityCount.put(severity, severityCount.getOrDefault(severity, 0) + 1);

            // Critical count
            if ("CRITICAL".equals(severity)) {
                totalCritical++;
            }

            // Health score by vehicle
            String vehicleId = report.getVehicleId();
            vehicleReports.put(vehicleId, vehicleReports.getOrDefault(vehicleId, 0) + 1);

            try {
                double score = Double.parseDouble(report.getHealthScore());
                double current = vehicleHealthScores.getOrDefault(vehicleId, 0.0);
                vehicleHealthScores.put(vehicleId, current + score);
                totalHealth += score;
            } catch (NumberFormatException e) {
                // Skip invalid scores
            }
        }

        // Update stats labels
        totalReportsLabel.setText(String.valueOf(historyData.size()));
        criticalReportsLabel.setText(String.valueOf(totalCritical));

        if (!historyData.isEmpty()) {
            avgHealthScoreLabel.setText(String.format("%.1f", totalHealth / historyData.size()));
        } else {
            avgHealthScoreLabel.setText("--");
        }

        // Update severity pie chart
        ObservableList<PieChart.Data> severityData = FXCollections.observableArrayList();
        severityCount.forEach((severity, count) -> {
            severityData.add(new PieChart.Data(severity + " (" + count + ")", count));
        });
        severityChart.setData(severityData);

        // Update health score bar chart
        XYChart.Series<String, Number> healthSeries = new XYChart.Series<>();
        healthSeries.setName("Avg Health Score");
        vehicleHealthScores.forEach((vehicleId, totalScore) -> {
            int reportCount = vehicleReports.get(vehicleId);
            double avgScore = totalScore / reportCount;
            healthSeries.getData().add(new XYChart.Data<>(vehicleId, avgScore));
        });

        healthScoreChart.getData().clear();
        if (!healthSeries.getData().isEmpty()) {
            healthScoreChart.getData().add(healthSeries);
        }
    }

    private void applyHistoryFilter() {
        String searchText = historySearchField.getText();
        String selectedSeverity = severityFilterCombo.getValue();

        filteredHistory.setPredicate(report -> {
            boolean matchesSearch = searchText == null || searchText.trim().isEmpty()
                    || report.getVehicleId().toLowerCase().contains(searchText.trim().toLowerCase());

            String severity = report.getSeverity() != null ? report.getSeverity() : "";
            boolean matchesSeverity = "All".equals(selectedSeverity)
                    || severity.equalsIgnoreCase(selectedSeverity);

            return matchesSearch && matchesSeverity;
        });
    }

    private java.util.List<DiagnosticReportHistory> readHistoryFromCsv() throws java.io.IOException {
        java.nio.file.Path csvPath = java.nio.file.Paths.get("data", Constants.CSV_FILE);

        try {
            String absolutePath = csvPath.toAbsolutePath().toString();
            LOGGER.info("Loading diagnostic history from CSV at: {}", absolutePath);
            System.out.println("Loading diagnostic history from CSV at: " + absolutePath);
        } catch (Exception e) {
            LOGGER.warn("Failed to log absolute path", e);
        }

        java.util.List<DiagnosticReportHistory> history = new java.util.ArrayList<>();

        if (!java.nio.file.Files.exists(csvPath)) {
            LOGGER.debug("CSV file does not exist at: {}, returning empty history", csvPath);
            return history;
        }

        try (java.io.BufferedReader reader = java.nio.file.Files.newBufferedReader(csvPath)) {
            org.apache.commons.csv.CSVParser parser = org.apache.commons.csv.CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withTrim()
                    .parse(reader);

            for (org.apache.commons.csv.CSVRecord record : parser) {
                try {
                    String timestamp = getRecordValue(record, "timestamp", "N/A");
                    String vehicleId = getRecordValue(record, "vehicleId", "Unknown");
                    String type = getRecordValue(record, "vehicleType", "Unknown");
                    String model = getRecordValue(record, "model", "Unknown");
                    String year = getRecordValue(record, "year", "Unknown");
                    String faultCode = getRecordValue(record, "faultCode", "N/A");
                    String healthScore = getRecordValue(record, "healthScore", "0");
                    String summary = getRecordValue(record, "overallSummary", "");

                    if (isHeaderRecord(timestamp, vehicleId, type, model, year, faultCode, healthScore, summary)) {
                        LOGGER.debug("Skipping header-like CSV record: {}", record.toString());
                        continue;
                    }

                    history.add(new DiagnosticReportHistory(
                            timestamp,
                            vehicleId,
                            type,
                            model,
                            year,
                            faultCode,
                            healthScore,
                            summary,
                            inferSeverity(summary)
                    ));
                } catch (Exception e) {
                    LOGGER.debug("Skipping malformed CSV record: {}", e.getMessage());
                }
            }
        }
        return history;
    }

    private String getRecordValue(org.apache.commons.csv.CSVRecord record, String columnName, String defaultValue) {
        try {
            if (record.isMapped(columnName)) {
                String value = record.get(columnName);
                return value != null && !value.trim().isEmpty() ? value : defaultValue;
            }
        } catch (IllegalArgumentException e) {
            // Column doesn't exist
        }
        return defaultValue;
    }

    private boolean isHeaderRecord(String timestamp, String vehicleId, String type, String model,
                                   String year, String faultCode, String healthScore, String summary) {
        return "timestamp".equalsIgnoreCase(timestamp)
                || "vehicleid".equalsIgnoreCase(vehicleId)
                || "type".equalsIgnoreCase(type)
                || "model".equalsIgnoreCase(model)
                || "year".equalsIgnoreCase(year)
                || "faultcode".equalsIgnoreCase(faultCode)
                || "healthscore".equalsIgnoreCase(healthScore)
                || "overallsummary".equalsIgnoreCase(summary)
                || "summary".equalsIgnoreCase(summary);
    }

    private String inferSeverity(String overallSummary) {
        if (overallSummary == null) {
            return "LOW";
        }
        String normalized = overallSummary.trim().toUpperCase();
        if (normalized.startsWith("CRITICAL")) {
            return "CRITICAL";
        }
        if (normalized.startsWith("WARNING")) {
            return "HIGH";
        }
        if (normalized.startsWith("MODERATE")) {
            return "MEDIUM";
        }
        return "LOW";
    }

    private Vehicle buildVehicleFromInput() {
        String type = vehicleTypeCombo.getValue();
        if ("Truck".equalsIgnoreCase(type)) {
            return new Truck(vehicleIdField.getText(), modelField.getText(), parseInt(yearField.getText(), "year"));
        } else {
            return new Car(vehicleIdField.getText(), modelField.getText(), parseInt(yearField.getText(), "year"));
        }
    }

    private double parseDouble(String value, String fieldName) {
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid " + fieldName + ": " + value);
        }
    }

    private int parseInt(String value, String fieldName) {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid " + fieldName + ": " + value);
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
