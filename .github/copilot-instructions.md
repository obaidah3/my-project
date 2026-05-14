# Copilot Instructions for vehicle-diagnostic-system

This repository is a Java Maven project for a multi-client vehicle diagnostic server with a console client and JavaFX dashboard.

## Key project facts
- Java 11+ with Maven build.
- Uses JavaFX 22.0.2, Apache Commons CSV, SLF4J, and Log4j2.
- Single-module Maven project; source code under src/main/java/com/vehiclediag.
- Tests are under src/test/java and run with JUnit 5.
- Persistent report storage is in data/DiagnosticReports.csv.
- The server listens on localhost:5000.

## Primary packages
- model — domain objects and payloads (Vehicle, DiagnosticRequest, DiagnosticReport).
- analyzer — analysis engine and analyzers.
- server — socket server and per-client request handling.
- client — console client and request builder.
- ui — JavaFX dashboard and styles.
- util — validation, constants, and logging helpers.

## Main entry points
- com.vehiclediag.server.DiagnosticServer — server.
- com.vehiclediag.client.ClientUI — console client.
- com.vehiclediag.ui.MainApp — JavaFX dashboard.

## Recommended commands
- mvn clean compile
- mvn test
- mvn package
- mvn exec:java -Dexec.mainClass=com.vehiclediag.server.DiagnosticServer
- mvn exec:java -Dexec.mainClass=com.vehiclediag.client.ClientUI
- mvn exec:java -Dexec.mainClass=com.vehiclediag.ui.MainApp

## Agent guidance
- Prefer preserving the existing architecture and package boundaries.
- Keep changes aligned with the server/client/UI separation.
- Do not duplicate README content; reference README.md for detailed overview and run instructions.
- Use data/DiagnosticReports.csv only when working on persistence or history behavior.
- When modifying UI, ensure JavaFX plugin compatibility and preserve src/main/resources/styles/dashboard.css styling.
