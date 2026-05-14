# AGENTS

This repository is a Java Maven project for a multi-client vehicle diagnostic system with a console client and JavaFX dashboard.

## Purpose
Use this file to explain how AI agents should operate in the repository, including the most important projects, workflows, and constraints.

## Key facts
- Java 11+ project built with Maven.
- Main code is in `src/main/java/com/vehiclediag`.
- Tests are in `src/test/java` and run with JUnit 5.
- Persistent data is stored in `data/DiagnosticReports.csv`.
- The server listens on `localhost:5000`.

## Primary workflows
- Build: `mvn clean compile`
- Test: `mvn test`
- Package: `mvn package`
- Run server: `mvn exec:java -Dexec.mainClass=com.vehiclediag.server.DiagnosticServer`
- Run console client: `mvn exec:java -Dexec.mainClass=com.vehiclediag.client.ClientUI`
- Run dashboard: `mvn exec:java -Dexec.mainClass=com.vehiclediag.ui.MainApp`

## Agent guidance
- Preserve the existing architecture and package boundaries.
- Keep changes aligned with server/client/UI separation.
- Avoid duplicating README content; refer to `README.md` for detailed overview and instructions.
- Use `data/DiagnosticReports.csv` only when changing persistence or report-history behavior.
- When modifying UI, preserve JavaFX compatibility and the existing styles under `src/main/resources/styles/dashboard.css`.

## Notes
- This repository already contains `.github/copilot-instructions.md` for Copilot guidance.
- There are additional planning and specification documents under `specs/001-vehicle-diagnostics`.
