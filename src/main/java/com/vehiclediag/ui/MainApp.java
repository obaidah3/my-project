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
import javafx.scene.control.Separator;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
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

    public static void main(String[] args) {
        launch(args);
    }

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
        root.setTop(buildTitleBar());
        root.setCenter(buildTabPane());

        String css = getClass().getResource("/styles/dashboard.css").toExternalForm();
        Scene scene = new Scene(root, 1200, 720);
        scene.getStylesheets().add(css);

        primaryStage.setScene(scene);
        primaryStage.setMinWidth(1000);
        primaryStage.setMinHeight(650);
        primaryStage.centerOnScreen();
        primaryStage.show();

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

    private TabPane buildTabPane() {
        TabPane tabPane = new TabPane();
        tabPane.getStyleClass().add("dashboard-tab-pane");

        Tab diagnosisTab = new Tab("New Diagnosis", buildDiagnosisTab());
        Tab historyTab = new Tab("History", buildHistoryTab());
        Tab analyticsTab = new Tab("Analytics", buildAnalyticsTab());

        diagnosisTab.setClosable(false);
        historyTab.setClosable(false);
        analyticsTab.setClosable(false);

        tabPane.getTabs().addAll(diagnosisTab, historyTab, analyticsTab);
        return tabPane;
    }

    private VBox buildDiagnosisTab() {
        // Left form section wrapped in a single scroll pane for smaller screens.
        VBox formContent = new VBox(12);
        formContent.setPadding(new Insets(12));
        formContent.setFillWidth(true);
        formContent.setMaxWidth(480);

        VBox vehicleSection = createCard("Vehicle Information");
        GridPane vehicleGrid = createFormGrid();
        vehicleIdField = createFormField();
        vehicleTypeCombo = new ComboBox<>();
        vehicleTypeCombo.getItems().addAll("Car", "Truck");
        vehicleTypeCombo.getSelectionModel().selectFirst();
        vehicleTypeCombo.getStyleClass().add("combo-box");
        modelField = createFormField();
        yearField = createFormField();
        vehicleGrid.add(createLabel("Vehicle ID:"), 0, 0);
        vehicleGrid.add(vehicleIdField, 1, 0);
        vehicleGrid.add(createLabel("Type:"), 0, 1);
        vehicleGrid.add(vehicleTypeCombo, 1, 1);
        vehicleGrid.add(createLabel("Model:"), 0, 2);
        vehicleGrid.add(modelField, 1, 2);
        vehicleGrid.add(createLabel("Year:"), 0, 3);
        vehicleGrid.add(yearField, 1, 3);
        vehicleSection.getChildren().add(vehicleGrid);

        VBox basicSection = createCard("Basic Sensors");
        GridPane basicGrid = createFormGrid();
        speedField = createFormField();
        rpmField = createFormField();
        temperatureField = createFormField();
        batteryVoltageField = createFormField();
        fuelLevelField = createFormField();
        basicGrid.add(createLabel("Speed (km/h):"), 0, 0);
        basicGrid.add(speedField, 1, 0);
        basicGrid.add(createLabel("RPM:"), 0, 1);
        basicGrid.add(rpmField, 1, 1);
        basicGrid.add(createLabel("Temp (°C):"), 0, 2);
        basicGrid.add(temperatureField, 1, 2);
        basicGrid.add(createLabel("Battery (V):"), 0, 3);
        basicGrid.add(batteryVoltageField, 1, 3);
        basicGrid.add(createLabel("Fuel (%):"), 0, 4);
        basicGrid.add(fuelLevelField, 1, 4);
        basicSection.getChildren().add(basicGrid);

        VBox advancedSection = createCard("Advanced Sensors");
        GridPane advancedGrid = createFormGrid();
        oilPressureField = createFormField();
        coolantLevelField = createFormField();
        transmissionTemperatureField = createFormField();
        throttlePositionField = createFormField();
        mafReadingField = createFormField();
        oxygenSensorVoltageField = createFormField();
        mileageField = createFormField();
        advancedGrid.add(createLabel("Oil Pressure:"), 0, 0);
        advancedGrid.add(oilPressureField, 1, 0);
        advancedGrid.add(createLabel("Coolant (%):"), 0, 1);
        advancedGrid.add(coolantLevelField, 1, 1);
        advancedGrid.add(createLabel("Trans Temp (°C):"), 0, 2);
        advancedGrid.add(transmissionTemperatureField, 1, 2);
        advancedGrid.add(createLabel("Throttle (%):"), 0, 3);
        advancedGrid.add(throttlePositionField, 1, 3);
        advancedGrid.add(createLabel("MAF:"), 0, 4);
        advancedGrid.add(mafReadingField, 1, 4);
        advancedGrid.add(createLabel("O2 Sensor (V):"), 0, 5);
        advancedGrid.add(oxygenSensorVoltageField, 1, 5);
        advancedGrid.add(createLabel("Mileage (km):"), 0, 6);
        advancedGrid.add(mileageField, 1, 6);
        advancedSection.getChildren().add(advancedGrid);

        VBox faultSection = createCard("Fault Code");
        GridPane faultGrid = createFormGrid();
        faultCodeField = createFormField();
        faultGrid.add(createLabel("Fault Code:"), 0, 0);
        faultGrid.add(faultCodeField, 1, 0);
        faultSection.getChildren().add(faultGrid);

        Button submitButton = new Button("Submit Diagnostic");
        submitButton.getStyleClass().add("dashboard-button");
        submitButton.setMaxWidth(Double.MAX_VALUE);
        submitButton.setOnAction(event -> handleSubmit());
        HBox buttonBox = new HBox(submitButton);
        buttonBox.setAlignment(Pos.CENTER);
        HBox.setHgrow(submitButton, Priority.ALWAYS);
        buttonBox.setPadding(new Insets(8, 0, 0, 0));

        formContent.getChildren().addAll(vehicleSection, basicSection, advancedSection, faultSection, buttonBox);

        ScrollPane leftScrollPane = new ScrollPane(formContent);
        leftScrollPane.setFitToWidth(true);
        leftScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        leftScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        leftScrollPane.setPrefViewportWidth(420);
        leftScrollPane.setMaxWidth(460);
        leftScrollPane.setStyle("-fx-background-color: transparent;");

        VBox resultColumn = new VBox(14);
        resultColumn.setFillWidth(true);
        resultColumn.setPadding(new Insets(12));

        VBox resultCard = createCard("Diagnosis Result");
        resultSummaryLabel = new Label("Awaiting diagnostic submission...");
        resultSummaryLabel.getStyleClass().add("result-summary");
        resultArea = new TextArea();
        resultArea.setEditable(false);
        resultArea.setWrapText(true);
        resultArea.getStyleClass().add("result-details");
        resultArea.setPrefHeight(320);
        resultArea.setMaxHeight(Double.MAX_VALUE);
        VBox.setVgrow(resultArea, Priority.ALWAYS);
        resultCard.getChildren().addAll(resultSummaryLabel, resultArea);

        VBox recommendationCard = createCard("Recommendations");
        recommendationsArea = new TextArea();
        recommendationsArea.setEditable(false);
        recommendationsArea.setWrapText(true);
        recommendationsArea.getStyleClass().add("result-recommendations");
        recommendationsArea.setPrefHeight(180);
        recommendationsArea.setMaxHeight(Double.MAX_VALUE);
        VBox.setVgrow(recommendationsArea, Priority.ALWAYS);
        recommendationCard.getChildren().add(recommendationsArea);

        resultColumn.getChildren().addAll(resultCard, recommendationCard);
        VBox.setVgrow(resultCard, Priority.ALWAYS);
        VBox.setVgrow(recommendationCard, Priority.ALWAYS);

        SplitPane splitPane = new SplitPane(leftScrollPane, resultColumn);
        splitPane.setDividerPositions(0.33);
        splitPane.setPrefWidth(1020);
        splitPane.setMaxWidth(Double.MAX_VALUE);
        SplitPane.setResizableWithParent(leftScrollPane, false);

        VBox container = new VBox(splitPane);
        container.setPadding(new Insets(0));
        VBox.setVgrow(splitPane, Priority.ALWAYS);
        return container;
    }

    private VBox buildHistoryTab() {
        VBox historyRoot = new VBox(12);
        historyRoot.setPadding(new Insets(16));

        HBox filterBar = new HBox(12);
        filterBar.setAlignment(Pos.CENTER_LEFT);
        filterBar.getStyleClass().add("history-filter-bar");

        Label searchLabel = createLabel("Search:");
        historySearchField = new TextField();
        historySearchField.setPromptText("Vehicle ID...");
        historySearchField.getStyleClass().add("form-field-small");
        historySearchField.setPrefWidth(180);

        Label severityLabel = createLabel("Severity:");
        severityFilterCombo = new ComboBox<>();
        severityFilterCombo.getItems().addAll("All", "CRITICAL", "HIGH", "MEDIUM", "LOW");
        severityFilterCombo.getSelectionModel().selectFirst();
        severityFilterCombo.getStyleClass().add("combo-box");
        severityFilterCombo.setPrefWidth(120);

        Button refreshButton = new Button("Refresh");
        refreshButton.getStyleClass().add("secondary-button");
        refreshButton.setOnAction(event -> loadHistory());

        filterBar.getChildren().addAll(searchLabel, historySearchField, severityLabel, severityFilterCombo, refreshButton);

        statusLabel = new Label("Loaded 0 reports");
        statusLabel.getStyleClass().add("status-label");

        historyTable = new TableView<>();
        historyTable.getStyleClass().add("history-table");
        historyTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        historyTable.setPlaceholder(new Label("No diagnostic history available"));

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

        VBox.setVgrow(historyTable, Priority.ALWAYS);
        historyRoot.getChildren().addAll(filterBar, statusLabel, historyTable);
        return historyRoot;
    }

    private VBox buildAnalyticsTab() {
        VBox analyticsRoot = new VBox(16);
        analyticsRoot.setPadding(new Insets(16));

        HBox metricsRow = new HBox(12);
        metricsRow.setFillHeight(true);

        totalReportsLabel = new Label("0");
        totalReportsLabel.getStyleClass().add("metric-value");
        criticalReportsLabel = new Label("0");
        criticalReportsLabel.getStyleClass().add("metric-value");
        avgHealthScoreLabel = new Label("--");
        avgHealthScoreLabel.getStyleClass().add("metric-value");

        metricsRow.getChildren().addAll(
                createMetricCard("Total Reports", totalReportsLabel),
                createMetricCard("Critical Reports", criticalReportsLabel),
                createMetricCard("Avg Health Score", avgHealthScoreLabel)
        );

        HBox chartsRow = new HBox(12);
        chartsRow.setFillHeight(true);

        severityChart = new PieChart();
        severityChart.setLegendVisible(false);
        severityChart.setLabelsVisible(true);
        severityChart.setPrefHeight(280);
        severityChart.setMaxHeight(280);
        severityChart.getStyleClass().add("analytics-chart");

        healthScoreChart = createHealthScoreChart();
        healthScoreChart.setPrefHeight(280);
        healthScoreChart.setMaxHeight(280);
        healthScoreChart.getStyleClass().add("analytics-chart");

        VBox severityCard = createCard("Severity Distribution");
        VBox.setVgrow(severityChart, Priority.ALWAYS);
        severityCard.getChildren().add(severityChart);

        VBox healthCard = createCard("Health Score by Vehicle");
        VBox.setVgrow(healthScoreChart, Priority.ALWAYS);
        healthCard.getChildren().add(healthScoreChart);

        chartsRow.getChildren().addAll(severityCard, healthCard);
        HBox.setHgrow(severityCard, Priority.ALWAYS);
        HBox.setHgrow(healthCard, Priority.ALWAYS);

        analyticsRoot.getChildren().addAll(metricsRow, chartsRow);
        VBox.setVgrow(chartsRow, Priority.ALWAYS);
        return analyticsRoot;
    }

    private VBox createMetricCard(String title, Label valueLabel) {
        VBox card = createCard(title);
        valueLabel.getStyleClass().add("metric-value");
        Label descriptor = new Label(title);
        descriptor.getStyleClass().add("metric-label");
        card.getChildren().addAll(valueLabel, descriptor);
        return card;
    }

    private BarChart<String, Number> createHealthScoreChart() {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Vehicle ID");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Avg Health Score");
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Avg Health Score by Vehicle");
        barChart.setLegendVisible(false);
        return barChart;
    }

    private TextField createFormField() {
        TextField field = new TextField();
        field.getStyleClass().add("form-field");
        field.setPrefHeight(34);
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
