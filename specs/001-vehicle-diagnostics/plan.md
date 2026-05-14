# Implementation Plan: Smart Vehicle Diagnostic & Fault Detection System

**Feature Branch**: `001-vehicle-diagnostics`  
**Created**: May 3, 2026  
**Completed**: May 6, 2026  
**Java Target Version**: Java 11+  
**Status**: ✅ COMPLETED & DEPLOYED  

---

## 1. Architecture Overview

### System Design Pattern: Client-Server with Thread-Per-Connection

The system follows a classic **client-server architecture** with **multi-threaded server processing**:

```
┌─────────────────────────────────────────────────────────────┐
│                    DIAGNOSTIC CLIENTS                       │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐                  │
│  │ Client 1 │  │ Client 2 │  │ Client 3 │  ...             │
│  └────┬─────┘  └────┬─────┘  └────┬─────┘                  │
└───────┼─────────────┼─────────────┼──────────────────────────┘
        │             │             │
        │  Socket     │  Socket     │  Socket
        │ Connection  │ Connection  │ Connection
        │             │             │
┌───────┴─────────────┴─────────────┴──────────────────────────┐
│                   SERVER (localhost:5000)                    │
│  ┌───────────────────────────────────────────────────────┐  │
│  │ DiagnosticServer (Main Thread)                        │  │
│  │  - Listens on port 5000                               │  │
│  │  - Accepts incoming connections                       │  │
│  │  - Spawns ClientHandler thread per connection         │  │
│  └───────────────────────────────────────────────────────┘  │
│                                                              │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │ ClientHandler│  │ ClientHandler│  │ ClientHandler│      │
│  │   (Thread 1) │  │   (Thread 2) │  │   (Thread 3) │      │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘      │
│         │                 │                 │              │
│  ┌──────┴─────────────────┴─────────────────┴──────┐       │
│  │ Diagnostic Analysis Engine                       │       │
│  │ ┌────────────────────────────────────────────┐  │       │
│  │ │ • EngineAnalyzer    (RPM, Temp)           │  │       │
│  │ │ • BatteryAnalyzer   (Voltage)             │  │       │
│  │ │ • FuelAnalyzer      (Level)               │  │       │
│  │ │ • FaultAnalyzer     (Fault Codes)         │  │       │
│  │ └────────────────────────────────────────────┘  │       │
│  └──────────────────────────────────────────────────┘       │
│                                                              │
│  ┌──────────────────────────────────────────────────┐       │
│  │ CSV Persistence Layer                            │       │
│  │ (DiagnosticReports.csv - Thread-safe writes)    │       │
│  └──────────────────────────────────────────────────┘       │
└──────────────────────────────────────────────────────────────┘
```

### Key Architectural Principles

1. **Separation of Concerns**: Each class has a single responsibility
   - Data Models (Vehicle, DiagnosticRequest, DiagnosticReport)
   - Analysis Engine (Analyzers interface and implementations)
   - Network Layer (Server, ClientHandler, Client)
   - Persistence Layer (CSV writer with synchronization)

2. **Polymorphism**: All analyzers implement the `Analyzer` interface
   - Allows adding new analyzer types without modifying existing code
   - ClientHandler invokes analyzers polymorphically in a loop

3. **Thread Safety**: CSV file access synchronized to prevent data corruption
   - Uses `ReentrantLock` or `synchronized` blocks for write operations
   - Each client handler runs independently without state sharing

4. **Serialization Protocol**: Java serialization for type safety
   - ObjectInputStream/ObjectOutputStream handle marshalling
   - Both DiagnosticRequest and DiagnosticReport are Serializable

5. **Graceful Degradation**: Comprehensive exception handling
   - Individual client failures don't affect server or other clients
   - All socket/stream operations wrapped in try-catch blocks

---

## 2. Package Structure

Organize code into logical, reusable packages:

```
src/main/java/com/vehiclediag/
│
├── model/                          # Data models (entities)
│   ├── Vehicle.java                # Abstract base class
│   ├── Car.java                    # Concrete Car type
│   ├── Truck.java                  # Concrete Truck type
│   ├── DiagnosticRequest.java      # Request payload (Serializable)
│   └── DiagnosticReport.java       # Response payload (Serializable)
│
├── analyzer/                       # Diagnostic analysis engine
│   ├── Analyzer.java               # Interface (contract)
│   ├── EngineAnalyzer.java         # RPM & temperature analysis
│   ├── BatteryAnalyzer.java        # Voltage analysis
│   ├── FuelAnalyzer.java           # Fuel level analysis
│   ├── FaultAnalyzer.java          # Fault code analysis
│   └── AnalysisResult.java         # Result carrier (optional helper)
│
├── server/                         # Server-side components
│   ├── DiagnosticServer.java       # Main server (listening thread)
│   ├── ClientHandler.java          # Per-client request handler
│   ├── DiagnosticProcessor.java    # Orchestrates analysis
│   └── ReportPersistence.java      # CSV file writer (thread-safe)
│
├── client/                         # Client-side components
│   ├── DiagnosticClient.java       # Main client application
│   ├── ClientUI.java               # Menu & input handling
│   └── RequestBuilder.java         # DiagnosticRequest construction
│
└── util/                           # Utility classes
    ├── ValidationUtil.java         # Input validation helpers
    ├── Constants.java              # System constants
    └── LoggerUtil.java             # Logging helpers

src/test/java/com/vehiclediag/
│
├── model/                          # Unit tests for models
│   ├── VehicleTest.java
│   ├── DiagnosticRequestTest.java
│   └── DiagnosticReportTest.java
│
├── analyzer/                       # Unit tests for analyzers
│   ├── EngineAnalyzerTest.java
│   ├── BatteryAnalyzerTest.java
│   ├── FuelAnalyzerTest.java
│   └── FaultAnalyzerTest.java
│
├── server/                         # Integration tests
│   ├── DiagnosticServerTest.java
│   ├── ClientHandlerTest.java
│   └── ReportPersistenceTest.java
│
└── integration/                    # End-to-end tests
    └── ClientServerIntegrationTest.java
```

### Package Responsibilities

| Package | Responsibility | Key Classes |
|---------|-----------------|-------------|
| **model** | Data representation and validation | Vehicle, Car, Truck, DiagnosticRequest, DiagnosticReport |
| **analyzer** | Diagnostic analysis logic | Analyzer interface, 4 implementations |
| **server** | Server-side network & processing | DiagnosticServer, ClientHandler, DiagnosticProcessor |
| **client** | Client-side UI and communication | DiagnosticClient, ClientUI, RequestBuilder |
| **util** | Cross-cutting concerns | Validation, Constants, Logging |

---

## 3. Implementation Phases

Implement in sequential phases, each building on previous phases. Each phase delivers testable functionality.

### Phase 0: Project Setup & Infrastructure (1 day)

**Objectives**: Create project structure, configure build, establish testing framework

**Tasks**:
1. Create Maven project structure (POM.xml configuration)
   - JUnit 5 for testing
   - Apache Commons CSV for safe CSV operations
   - SLF4J for logging (Log4j2 implementation)

2. Create package directories per structure above

3. Set up basic CI configuration if available

4. Create Constants.java with all system-wide constants:
   ```java
   public class Constants {
       public static final int SERVER_PORT = 5000;
       public static final String CSV_FILE = "DiagnosticReports.csv";
       public static final int MAX_CLIENTS = 50;
       public static final int RESPONSE_TIMEOUT_MS = 3000;
       
       // Sensor ranges
       public static final int MAX_SPEED_KMH = 250;
       public static final int MAX_RPM = 8000;
       public static final double MAX_TEMP_C = 120.0;
       public static final double MIN_TEMP_C = 50.0;
       // ... etc
   }
   ```

**Deliverable**: Compiling project with test framework ready

---

### Phase 1: Core Data Models (1 day)

**Objectives**: Implement all entity classes with validation; these are used by all other components

**Tasks**:

1. **Vehicle Hierarchy** (Abstract Polymorphism)
   - Create abstract `Vehicle` class with properties: vehicleID, vehicleType (enum), model, year
   - Create `Car` class extending Vehicle
   - Create `Truck` class extending Vehicle
   - Implement constructors with parameter validation
   - Add getters for all properties

2. **DiagnosticRequest** (Data Container, Serializable)
   - Properties: vehicle, speed, rpm, engineTemperature, batteryVoltage, fuelLevel, faultCode
   - Implement `Serializable` interface
   - Add validation method `validate()` that checks all fields
   - Throw `IllegalArgumentException` with descriptive messages for invalid data
   - Example validation:
     ```java
     if (speed < 0 || speed > Constants.MAX_SPEED_KMH) {
         throw new IllegalArgumentException("Speed must be 0-250 km/h");
     }
     ```

3. **DiagnosticReport** (Result Container, Serializable)
   - Properties: timestamp, vehicle, all sensor readings, engineCondition, batteryCondition, fuelCondition, overallSummary, faultDetails
   - Implement `Serializable` interface
   - Constructor takes all parameters and sets timestamp to now
   - Add methods: getters for all properties, `toString()` for formatted output

4. **ValidationUtil** (Helper class)
   - Static methods for reusable validations:
     - `validateVehicleID(String)`
     - `validateSpeed(double)`
     - `validateRPM(int)`
     - `validateTemperature(double)`
     - `validateBatteryVoltage(double)`
     - `validateFuelLevel(double)`

**Test Coverage**:
- Unit tests for Vehicle constructor validation
- Unit tests for Car/Truck instantiation
- Unit tests for DiagnosticRequest validation (valid & invalid inputs)
- Unit tests for DiagnosticReport creation
- Serialization round-trip tests

**Deliverable**: Core data models fully implemented and tested

---

### Phase 2: Analysis Engine (1.5 days)

**Objectives**: Implement diagnostic analyzers with proper polymorphic design; test independently

**Tasks**:

1. **Analyzer Interface** (Contract)
   ```java
   public interface Analyzer {
       String analyze(DiagnosticRequest request);
   }
   ```

2. **EngineAnalyzer** (Implementation)
   - Evaluates RPM and engineTemperature
   - Logic:
     - Good Condition: RPM 1000-6000 AND Temperature 80-100°C
     - Moderate Condition: RPM 6000-7500 OR Temperature 100-115°C
     - Critical Condition: RPM > 7500 OR Temperature > 115°C
   - Returns condition string with explanation

3. **BatteryAnalyzer** (Implementation)
   - Evaluates batteryVoltage
   - Logic:
     - Good Condition: 13-14.5V
     - Acceptable Condition: 12-13V
     - Critical Condition: < 12V or > 14.5V
   - Returns condition string with explanation

4. **FuelAnalyzer** (Implementation)
   - Evaluates fuelLevel
   - Logic:
     - Normal: >= 15%
     - Warning: < 15% (returns recommendation to refuel)
   - Returns fuel status with recommendation

5. **FaultAnalyzer** (Implementation)
   - Evaluates faultCode
   - Maintains map of common fault codes to descriptions
   - Returns fault description and severity
   - Handles unknown codes gracefully

6. **DiagnosticProcessor** (Orchestrator)
   - Maintains list of Analyzer implementations
   - Method `processRequest(DiagnosticRequest)` → `DiagnosticReport`
   - Invokes each analyzer polymorphically
   - Combines results into comprehensive report
   - Calculates `overallSummary` based on individual conditions

**Test Coverage**:
- Unit tests for each analyzer with valid/invalid inputs
- Unit tests for boundary conditions (e.g., RPM = 6000, 7500)
- Unit tests for DiagnosticProcessor with various input combinations
- Tests for unknown fault codes

**Deliverable**: Fully tested diagnostic analysis engine

---

### Phase 3: Server Infrastructure (2 days)

**Objectives**: Implement multi-threaded server with robust connection handling

**Tasks**:

1. **ReportPersistence** (Thread-safe CSV Writer)
   - Maintains lock for synchronization (`ReentrantLock` or `synchronized`)
   - Method `appendReport(DiagnosticReport)` → appends to CSV file
   - Handles file creation with header row if doesn't exist
   - Uses try-catch for IOException
   - Thread-safe writing to prevent concurrent corruption
   - Example structure:
     ```java
     private ReentrantLock lock = new ReentrantLock();
     
     public void appendReport(DiagnosticReport report) {
         lock.lock();
         try {
             // Write to CSV file
         } finally {
             lock.unlock();
         }
     }
     ```

2. **ClientHandler** (Per-client Request Handler, Runnable)
   - Constructor receives: client Socket, Analyzer list, ReportPersistence
   - Method `run()` implements request-response loop:
     1. Open ObjectInputStream/ObjectOutputStream from socket
     2. Wait for DiagnosticRequest from client
     3. Validate request (catch exceptions)
     4. Create DiagnosticProcessor and invoke analyzers
     5. Generate DiagnosticReport
     6. Persist report to CSV via ReportPersistence
     7. Send DiagnosticReport back to client via ObjectOutputStream
     8. Close streams and socket gracefully
   - Exception handling:
     - EOFException: client disconnected (log and exit cleanly)
     - IOException: socket error (log and exit)
     - ClassNotFoundException: serialization error (send error response)
     - InvalidClassException: incompatible class (send error response)
   - Uses try-finally for resource cleanup

3. **DiagnosticServer** (Main Server Thread, Runnable)
   - Constructor: port number
   - Method `start()`:
     1. Create ServerSocket on configured port
     2. Loop to accept incoming connections:
        - Accept client socket
        - Create ClientHandler with socket
        - Execute ClientHandler in thread pool (ExecutorService with fixed thread pool size)
        - Log connection with timestamp and thread info
     3. Handle shutdown gracefully (close ServerSocket)
   - Exception handling:
     - BindException: port already in use
     - IOException: accept() errors
   - Logs all connections and disconnections
   - Example structure:
     ```java
     private ExecutorService executor = Executors.newFixedThreadPool(
         Constants.MAX_CLIENTS);
     
     public void start() {
         serverSocket = new ServerSocket(port);
         while (running) {
             Socket clientSocket = serverSocket.accept();
             ClientHandler handler = new ClientHandler(clientSocket, analyzers, persistence);
             executor.execute(handler);
         }
     }
     ```

4. **Main.java for Server**
   ```java
   public class DiagnosticServer {
       public static void main(String[] args) {
           // Create analyzers
           List<Analyzer> analyzers = Arrays.asList(
               new EngineAnalyzer(),
               new BatteryAnalyzer(),
               new FuelAnalyzer(),
               new FaultAnalyzer()
           );
           
           // Create server
           DiagnosticServer server = new DiagnosticServer(
               Constants.SERVER_PORT, analyzers);
           server.start();
       }
   }
   ```

**Test Coverage**:
- Integration tests: start server, connect single client, send request, verify response
- Integration tests: connect 3+ concurrent clients, send simultaneous requests, verify all receive responses
- Test CSV file creation and data integrity after concurrent writes
- Test client disconnection (kill socket) and verify server continues accepting new clients
- Test malformed requests (serialization errors)

**Deliverable**: Production-ready multi-threaded server

---

### Phase 4: Client Application (1.5 days)

**Objectives**: Implement user-friendly console client for diagnostic data submission

**Tasks**:

1. **ClientUI** (Menu System)
   - Method `displayMainMenu()`: show vehicle type selection (Car/Truck)
   - Method `collectVehicleData()`: prompt for vehicle ID, model, year with validation
   - Method `collectSensorReadings()`: prompt for each sensor value with range validation
     - Speed prompt: validate 0-250
     - RPM prompt: validate 0-8000
     - Temperature prompt: validate 50-120°C
     - Battery voltage prompt: validate 8-16V
     - Fuel level prompt: validate 0-100%
     - Fault code prompt: optional input
   - Method `displayReport(DiagnosticReport)`: format and display report
   - All methods handle `InputMismatchException` and `NumberFormatException`
   - Clear user-friendly error messages with range information

2. **RequestBuilder** (Helper)
   - Method `buildRequest(Map<String, Object> inputs)` → DiagnosticRequest
   - Handles vehicle type enum parsing
   - Validates all inputs and throws descriptive exceptions
   - Used by ClientUI to construct request from user input

3. **DiagnosticClient** (Main Client)
   - Constructor: server host, port
   - Method `connect()`: create Socket and streams
   - Method `sendRequest(DiagnosticRequest)`: serialize and send request
   - Method `receiveReport()` → DiagnosticReport: deserialize response
   - Method `run()`: main loop
     1. Connect to server
     2. Display menu and collect vehicle data (ClientUI)
     3. Build DiagnosticRequest (RequestBuilder)
     4. Send request and receive report (methods above)
     5. Display report (ClientUI)
     6. Ask user: "Submit another diagnostic? (Y/N)"
     7. Loop or exit
   - Exception handling:
     - ConnectException: server not running (suggest starting server first)
     - IOException: connection lost
     - ClassNotFoundException: deserialize error
     - Partial failures don't crash entire application

4. **Main.java for Client**
   ```java
   public class DiagnosticClientApp {
       public static void main(String[] args) {
           String host = "localhost";
           int port = Constants.SERVER_PORT;
           
           try {
               DiagnosticClient client = new DiagnosticClient(host, port);
               client.run();
           } catch (ConnectException e) {
               System.err.println("Cannot connect to server at " 
                   + host + ":" + port);
               System.err.println("Please ensure server is running first.");
           }
       }
   }
   ```

**Test Coverage**:
- Unit tests: ClientUI input parsing with valid/invalid inputs
- Unit tests: RequestBuilder with various vehicle and sensor data
- Integration tests: single client connects to server, submits request, receives response
- Integration tests: client handles server disconnection gracefully
- Integration tests: malformed input doesn't crash client

**Deliverable**: User-friendly client application fully tested

---

### Phase 5: Integration & End-to-End Testing (2 days)

**Objectives**: Verify complete system functionality, concurrent operations, edge cases

**Tasks**:

1. **End-to-End Test Suite**
   - Test 1: Single client submits diagnostic, receive report, verify CSV file
   - Test 2: 3 concurrent clients submit simultaneously, all receive responses
   - Test 3: Client disconnects mid-stream, server continues normally
   - Test 4: CSV file handles 100+ concurrent writes without corruption
   - Test 5: Invalid vehicle ID handling across all layers
   - Test 6: Out-of-range sensor values rejected with user-friendly messages
   - Test 7: Multiple diagnostic sessions on same client
   - Test 8: Server shutdown and restart preserves CSV data

2. **Performance Testing**
   - Verify single request processes within 3 seconds (SC-001)
   - Verify 5 concurrent requests complete within 10 seconds (SC-002)
   - Monitor CSV file for data integrity after 100 writes

3. **Load Testing**
   - Simulate 10+ concurrent clients
   - Monitor thread count and memory usage
   - Verify no thread leaks

4. **Edge Case Testing**
   - Missing fields in request
   - Negative sensor values
   - Extremely large values (e.g., RPM = 50000)
   - Same vehicle ID from multiple clients simultaneously
   - Fault codes with special characters
   - Empty fault code
   - CSV file deletion during operation

**Deliverable**: Complete system validated against all requirements and success criteria

---

### Phase 6: Documentation & Code Review (1 day)

**Objectives**: Document system design, API contracts, deployment procedures

**Tasks**:

1. Create **ARCHITECTURE.md**: System design overview (similar to Section 1)
2. Create **API_CONTRACTS.md**: Interface documentation for each public class
3. Create **DEPLOYMENT.md**: Step-by-step setup and launch instructions
4. Create **TROUBLESHOOTING.md**: Common issues and solutions
5. Add inline code documentation (JavaDoc) for all public methods
6. Code review: ensure all code meets style guidelines

**Deliverable**: Professional documentation suite

---

## 4. Technology Choices

### Java Platform

**Choice**: Java 11 (LTS) or Java 17+ (newer LTS)
- **Rationale**: Both provide stable, long-term support; widely used in enterprise
- **Libraries**:
  - Java built-in: `java.net.Socket`, `java.io` (for serialization), `java.util.concurrent`
  - **JUnit 5**: Modern testing framework
  - **Apache Commons CSV**: Safe CSV writing with proper escaping
  - **SLF4J + Log4j2**: Industry-standard logging
  - **Maven**: Build and dependency management

### Design Patterns

| Pattern | Usage | Benefit |
|---------|-------|---------|
| **Strategy Pattern** | Analyzer interface with implementations | Easy to add new analyzers without modifying existing code |
| **Template Method** | ClientHandler and DiagnosticServer base structure | Consistent request-response flow |
| **Singleton** | Constants, LoggerFactory | Single source of truth |
| **Factory** | RequestBuilder | Encapsulate object construction logic |
| **Thread Pool** | ExecutorService in DiagnosticServer | Efficient thread management, bounded concurrency |
| **Thread-Safe Lazy Init** | ReportPersistence file creation | Safe first-write to CSV |

### Concurrency Approach

**Multi-threaded with Thread Pool**:
- Server accepts connections in main thread
- Each client handled by dedicated thread from ExecutorService
- Fixed thread pool size = Constants.MAX_CLIENTS
- CSV writes protected by ReentrantLock
- No shared state between client handlers

**Why not:**
- **Async/Await (Project Reactor)**: Overkill for this scope; thread-per-connection is simpler and sufficient
- **Virtual Threads (Java 19+)**: Good future option but Java 11 compatibility preferred for now

### Serialization

**Choice**: Java built-in serialization with ObjectInputStream/ObjectOutputStream
- **Rationale**: 
  - Specified in requirements (FR-023, FR-024)
  - Type-safe: ensures only DiagnosticRequest/Report types transmitted
  - No additional library dependencies
  - Sufficient for LAN environment
- **Trade-off**: Less human-readable than JSON, but type safety is more important here

### CSV Persistence

**Choice**: Apache Commons CSV library
- **Rationale**: Handles edge cases (quotes in data, commas, escaping) automatically
- **Alternative**: Custom CSV writer (rejected: error-prone, Commons CSV is lightweight)

### Logging

**Choice**: SLF4J with Log4j2 implementation
- **Rationale**: Industry-standard, flexible configuration, minimal boilerplate
- **Benefits**: Easy to switch implementations, configurable log levels, separate logger for each class

---

## 5. Key Design Decisions

### 1. Thread-Per-Client Model vs. Async I/O

**Decision**: Thread-per-client with ExecutorService thread pool

**Rationale**:
- Simpler code: each request handled sequentially in handler's run() method
- Easier to debug: call stack clearly shows request flow
- Sufficient for 50 concurrent clients (SC-002 requirement)
- Thread pool bounded to prevent resource exhaustion

**Trade-offs**:
- Higher memory per connection (~1MB per thread)
- Suitable up to ~100 concurrent connections
- If system needs >100 clients or <10ms latency, revisit async model

---

### 2. Java Serialization for Network Protocol

**Decision**: Use ObjectInputStream/ObjectOutputStream

**Rationale**:
- Requirement FR-023, FR-024 explicitly specify Java serialization
- Type-safe: prevents ClassNotFoundException for DiagnosticRequest type
- Server knows exact structure of incoming data
- Simpler than JSON parsing and validation

**Trade-offs**:
- Not human-readable (can't inspect with netcat)
- Tied to Java implementation (can't easily create non-Java client)
- Larger message size than JSON
- But: Network is local (LAN), and type safety outweighs these concerns

---

### 3. CSV File as Persistence Layer

**Decision**: CSV file instead of database

**Rationale**:
- Requirement FR-028 specifies CSV file persistence
- No external dependencies (database setup, licensing)
- Simple audit trail (human-readable)
- Easy to export/analyze with Excel/Python

**Trade-offs**:
- Limited query capability (would need full table load)
- Slower for large datasets (thousands of records)
- But: Current scope doesn't require querying; CSV is adequate for v1

**Future Enhancement**: Could replace with lightweight SQLite or in-memory H2 if querying becomes needed

---

### 4. Synchronized Block vs. ReentrantLock for CSV Writes

**Decision**: ReentrantLock in ReportPersistence class

**Rationale**:
- Clearer intent (lock/unlock explicitly visible)
- Better performance than synchronized for this use case
- Allows future timeout strategies if needed: `lock.tryLock(timeout, unit)`

**Example**:
```java
private ReentrantLock lock = new ReentrantLock();

public void appendReport(DiagnosticReport report) {
    lock.lock();
    try {
        // Write to CSV
        writer.writeRecord(reportToCSVRecord(report));
    } finally {
        lock.unlock();
    }
}
```

---

### 5. Stateless Analyzers (No State Sharing)

**Decision**: Analyzers are stateless, created once and reused

**Rationale**:
- Thread-safe: multiple ClientHandler threads can invoke same analyzer instance
- Efficient: single EngineAnalyzer instance serves all clients
- Simple testing: no mock state to manage

**Example**:
```java
// In DiagnosticServer.main():
List<Analyzer> analyzers = Arrays.asList(
    new EngineAnalyzer(),      // Reused by all clients
    new BatteryAnalyzer(),
    new FuelAnalyzer(),
    new FaultAnalyzer()
);

// In ClientHandler.run():
for (Analyzer analyzer : analyzers) {
    String result = analyzer.analyze(request);  // Thread-safe
}
```

---

### 6. Validation at Multiple Layers

**Decision**: Validate at both client and server

**Validation Layers**:
1. **Client (UI)**: Pre-submission validation
   - User sees error immediately
   - Prevents invalid data transmission
   - Example: RPM input prompts "Enter RPM (0-8000):"

2. **Client (RequestBuilder)**: Before serialization
   - Ensures DiagnosticRequest object is valid
   - Throws IllegalArgumentException with descriptive message

3. **Server (ClientHandler)**: Before analysis
   - Catch-all for tampered requests
   - Provides defense-in-depth
   - Sends error response to client

**Rationale**: Multiple validation points ensure robustness and user experience

---

### 7. Graceful Shutdown on Client Disconnection

**Decision**: ClientHandler catches EOFException and exits cleanly

**Rationale**:
- Client disconnect (Ctrl+C) sends EOF to ObjectInputStream
- Server logs disconnect but continues accepting new connections
- No resource leak: finally block closes socket/streams

**Example**:
```java
try {
    DiagnosticRequest request = (DiagnosticRequest) ois.readObject();
    // ... process request
} catch (EOFException e) {
    logger.info("Client disconnected: " + e.getMessage());
    return;  // Exit handler, close streams in finally
} finally {
    ois.close();
    oos.close();
    socket.close();
}
```

---

### 8. Exception Handling Strategy

**Decision**: Catch specific exceptions, log comprehensively, provide user-friendly messages

**Strategy**:
- **IOException**: Network error, log and disconnect
- **EOFException**: Expected on client disconnect, log as info
- **ClassNotFoundException**: Deserialization error, send error response
- **InvalidClassException**: Version mismatch, send error response
- **NumberFormatException**: User input validation, show range to user
- **InputMismatchException**: User input type mismatch, re-prompt user

**Example**:
```java
try {
    int rpm = scanner.nextInt();
} catch (InputMismatchException e) {
    scanner.nextLine();  // Consume invalid input
    System.err.println("Please enter a number between 0 and 8000");
    // Re-prompt
}
```

---

## 6. Risk Mitigation

### Risk 1: Concurrent CSV Writes Corrupting Data (HIGH)

**Risk**: Multiple ClientHandler threads writing to CSV simultaneously corrupt file or lose records

**Mitigation Strategy**:
1. **ReentrantLock**: All CSV writes protected by lock
2. **Atomic Append**: Single write operation, not multiple writes per record
3. **Test Thoroughly**: Load test with 20+ concurrent clients writing simultaneously
4. **Verify Headers**: CSV write always includes headers on first write

**Implementation Example**:
```java
lock.lock();
try {
    if (!csvFileExists()) {
        writer.writeHeaders();
    }
    writer.writeRecord(data);
    writer.flush();
} finally {
    lock.unlock();
}
```

**Verification**:
- Write 1000 records from 10 concurrent threads
- Verify final CSV has exactly 1000 data rows + 1 header row
- Verify no truncated or missing records

---

### Risk 2: Socket Resource Leaks (MEDIUM)

**Risk**: Exception in ClientHandler causes socket/stream to not close, exhausting socket pool

**Mitigation Strategy**:
1. **Try-Finally**: All socket operations in try-finally block
2. **Close Order**: Close ObjectOutputStream → ObjectInputStream → Socket
3. **Test Disconnection**: Kill client process, verify socket released

**Implementation Example**:
```java
Socket socket = null;
ObjectOutputStream oos = null;
ObjectInputStream ois = null;

try {
    socket = serverSocket.accept();
    oos = new ObjectOutputStream(socket.getOutputStream());
    ois = new ObjectInputStream(socket.getInputStream());
    // ... handle request
} catch (IOException e) {
    logger.error("Error handling client", e);
} finally {
    closeQuietly(oos);
    closeQuietly(ois);
    closeQuietly(socket);
}

private void closeQuietly(AutoCloseable resource) {
    if (resource != null) {
        try {
            resource.close();
        } catch (Exception e) {
            logger.warn("Error closing resource", e);
        }
    }
}
```

---

### Risk 3: Thread Pool Exhaustion (MEDIUM)

**Risk**: Many clients connect but don't disconnect, thread pool becomes exhausted

**Mitigation Strategy**:
1. **Fixed Pool Size**: Set to MAX_CLIENTS = 50
2. **Socket Timeout**: Set socket SO_TIMEOUT to detect hanging clients
3. **Monitor Thread Count**: Log thread count on each connection
4. **Graceful Degradation**: New connections fail with ConnectException if pool full

**Implementation**:
```java
ExecutorService executor = Executors.newFixedThreadPool(Constants.MAX_CLIENTS);

public void start() {
    serverSocket = new ServerSocket(port);
    while (running) {
        Socket clientSocket = serverSocket.accept();
        clientSocket.setSoTimeout(30000);  // 30 second timeout
        
        ClientHandler handler = new ClientHandler(clientSocket, ...);
        executor.execute(handler);
        
        logger.info("Active threads: " + 
            ((ThreadPoolExecutor) executor).getActiveCount());
    }
}
```

---

### Risk 4: ClassCastException on Serialization (MEDIUM)

**Risk**: Client sends object that's not DiagnosticRequest; server crashes on cast

**Mitigation Strategy**:
1. **Instanceof Check**: Verify object type before processing
2. **Catch ClassCastException**: Handle gracefully
3. **Send Error Response**: Inform client of protocol violation

**Implementation**:
```java
try {
    Object obj = ois.readObject();
    if (!(obj instanceof DiagnosticRequest)) {
        oos.writeObject(new ErrorResponse("Invalid request type"));
        return;
    }
    DiagnosticRequest request = (DiagnosticRequest) obj;
} catch (ClassNotFoundException | ClassCastException e) {
    logger.error("Serialization error", e);
    // Send error response
}
```

---

### Risk 5: CSV File I/O Exceptions (MEDIUM)

**Risk**: CSV file doesn't exist, permissions denied, disk full → write fails

**Mitigation Strategy**:
1. **Lazy Initialization**: Create CSV file on first write, not on startup
2. **Check Permissions**: Verify write access to directory on startup
3. **Handle Gracefully**: Log error, continue accepting clients (some diagnostics may be lost but system stays up)
4. **Fallback Path**: Allow configurable CSV path (e.g., environment variable)

**Implementation**:
```java
public void appendReport(DiagnosticReport report) {
    lock.lock();
    try {
        if (!Files.exists(csvPath)) {
            Files.createFile(csvPath);
            writer = new CSVWriter(new FileWriter(csvPath));
            writer.writeNext(CSV_HEADERS);
        }
        writer.writeNext(reportToArray(report));
        writer.flush();
    } catch (IOException e) {
        logger.error("Failed to persist report", e);
        // Continue processing; client gets response even if CSV fails
    } finally {
        lock.unlock();
    }
}
```

---

### Risk 6: Invalid User Input Crashing Client (MEDIUM)

**Risk**: User enters non-numeric value for RPM; NumberFormatException crashes client

**Mitigation Strategy**:
1. **Try-Catch in Menu**: Catch InputMismatchException and NumberFormatException
2. **Clear Prompts**: Show valid range with each prompt
3. **Re-prompt**: Loop until valid input received
4. **No Crashes**: Never terminate client due to user input

**Implementation**:
```java
public int promptRPM() {
    while (true) {
        try {
            System.out.print("Enter RPM (0-8000): ");
            int rpm = scanner.nextInt();
            scanner.nextLine();  // Consume newline
            
            if (rpm < 0 || rpm > 8000) {
                System.err.println("RPM must be 0-8000");
                continue;
            }
            return rpm;
        } catch (InputMismatchException e) {
            scanner.nextLine();  // Consume invalid input
            System.err.println("Please enter a valid number");
        }
    }
}
```

---

### Risk 7: Memory Exhaustion with Large CSV File (LOW)

**Risk**: CSV grows very large (100,000+ rows); reading entire file into memory fails

**Mitigation Strategy**:
1. **Append-Only**: Current implementation only appends, doesn't read entire file
2. **Stream Processing**: If querying added, use streaming CSV reader
3. **Archive Old Data**: Add periodic CSV rotation (backup and start new file)

**Not applicable for Phase 1** but note for future enhancement

---

### Risk 8: Port Already in Use (MEDIUM)

**Risk**: Server fails to start because port 5000 already in use

**Mitigation Strategy**:
1. **Descriptive Error**: Catch BindException and suggest solution
2. **Configurable Port**: Allow port as command-line argument
3. **Failover**: Try alternative port if primary fails

**Implementation**:
```java
public static void main(String[] args) {
    int port = args.length > 0 ? Integer.parseInt(args[0]) 
        : Constants.SERVER_PORT;
    
    try {
        DiagnosticServer server = new DiagnosticServer(port);
        server.start();
    } catch (BindException e) {
        System.err.println("Port " + port + " already in use");
        System.err.println("Try: java DiagnosticServer 5001");
        System.exit(1);
    }
}
```

---

## 7. Testing Strategy

### Unit Testing

**Test Framework**: JUnit 5 with AssertJ for fluent assertions

**Coverage by Component**:

#### Model Tests
```java
@Test
void testCarInstantiation_Valid() {
    Car car = new Car("V001", "Toyota", 2022);
    assertThat(car.getVehicleID()).isEqualTo("V001");
    assertThat(car.getModel()).isEqualTo("Toyota");
}

@Test
void testDiagnosticRequestValidation_InvalidSpeed() {
    DiagnosticRequest request = new DiagnosticRequest(..., 300, ...);
    assertThatThrownBy(request::validate)
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Speed must be 0-250");
}
```

**Goal**: 100% code coverage for model validation

#### Analyzer Tests
```java
@Test
void testEngineAnalyzer_GoodCondition() {
    EngineAnalyzer analyzer = new EngineAnalyzer();
    DiagnosticRequest request = createRequest(2000, 90);  // 2000 RPM, 90°C
    
    String result = analyzer.analyze(request);
    
    assertThat(result).contains("Good Condition");
}

@Test
void testEngineAnalyzer_CriticalTemperature() {
    DiagnosticRequest request = createRequest(5000, 118);  // 118°C
    String result = new EngineAnalyzer().analyze(request);
    assertThat(result).contains("Critical Condition");
}
```

**Goal**: 100% branch coverage for all analyzer logic

### Integration Testing

**Test Framework**: JUnit 5 with manual socket setup

#### Client-Handler Communication Test
```java
@Test
void testClientServerCommunication_ValidRequest() throws Exception {
    // Setup: Start server in background thread
    DiagnosticServer server = new DiagnosticServer(5001);
    new Thread(() -> server.start()).start();
    
    // Act: Connect client, send request
    Socket socket = new Socket("localhost", 5001);
    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
    
    DiagnosticRequest request = createValidRequest();
    oos.writeObject(request);
    oos.flush();
    
    DiagnosticReport response = (DiagnosticReport) ois.readObject();
    
    // Assert: Response contains expected data
    assertThat(response.getOverallSummary()).isNotEmpty();
    assertThat(response.getVehicle().getVehicleID())
        .isEqualTo(request.getVehicle().getVehicleID());
    
    socket.close();
    server.stop();
}
```

#### Concurrent Client Test
```java
@Test
void testMultipleConcurrentClients() throws Exception {
    DiagnosticServer server = new DiagnosticServer(5002);
    new Thread(() -> server.start()).start();
    
    // Create 5 clients and submit requests concurrently
    List<Thread> threads = new ArrayList<>();
    List<DiagnosticReport> responses = Collections.synchronizedList(new ArrayList<>());
    
    for (int i = 0; i < 5; i++) {
        threads.add(new Thread(() -> {
            try {
                DiagnosticReport report = submitRequest("V" + i);
                responses.add(report);
            } catch (Exception e) {
                fail("Client failed: " + e.getMessage());
            }
        }));
    }
    
    threads.forEach(Thread::start);
    threads.forEach(t -> t.join(10000));
    
    // All clients should receive responses
    assertThat(responses).hasSize(5);
}
```

#### CSV Persistence Test
```java
@Test
void testCSVPersistence_ConcurrentWrites() throws Exception {
    ReportPersistence persistence = new ReportPersistence("test-reports.csv");
    
    // Write 100 reports from 10 concurrent threads
    List<Thread> threads = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
        int threadNum = i;
        threads.add(new Thread(() -> {
            for (int j = 0; j < 10; j++) {
                DiagnosticReport report = createReport("V" + threadNum + "_" + j);
                persistence.appendReport(report);
            }
        }));
    }
    
    threads.forEach(Thread::start);
    threads.forEach(t -> t.join());
    
    // Verify CSV has exactly 100 data rows + 1 header row
    List<String> lines = Files.readAllLines(Paths.get("test-reports.csv"));
    assertThat(lines).hasSize(101);  // 100 data + 1 header
}
```

### Performance Testing

```java
@Test
void testSingleRequestLatency() throws Exception {
    // Verify single request processes within 3 seconds (SC-001)
    Instant start = Instant.now();
    
    DiagnosticRequest request = createValidRequest();
    DiagnosticReport response = submitRequest(request);
    
    Duration elapsed = Duration.between(start, Instant.now());
    assertThat(elapsed).isLessThan(Duration.ofSeconds(3));
}

@Test
void testConcurrentRequestLatency() throws Exception {
    // Verify 5 concurrent requests complete within 10 seconds (SC-002)
    Instant start = Instant.now();
    
    List<Future<DiagnosticReport>> futures = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
        futures.add(submitRequestAsync(createValidRequest(i)));
    }
    
    for (Future<DiagnosticReport> future : futures) {
        future.get();  // Wait for all to complete
    }
    
    Duration elapsed = Duration.between(start, Instant.now());
    assertThat(elapsed).isLessThan(Duration.ofSeconds(10));
}
```

### Test Data Builders

Create reusable test data builders to reduce boilerplate:

```java
public class TestDataBuilder {
    public static DiagnosticRequest validRequest() {
        return new DiagnosticRequest(
            new Car("TEST001", "Toyota", 2022),
            50,      // speed
            2000,    // rpm
            90,      // temp
            13.5,    // battery
            75,      // fuel
            ""       // fault code
        );
    }
    
    public static DiagnosticRequest invalidRequest_HighRPM() {
        return new DiagnosticRequest(
            new Car("TEST002", "BMW", 2021),
            100,
            9000,    // Invalid: > 8000
            95,
            13,
            50,
            ""
        );
    }
}
```

### Test Execution

**Running Tests**:
```bash
# All tests
mvn test

# Specific test class
mvn test -Dtest=EngineAnalyzerTest

# With coverage report
mvn test jacoco:report

# Performance tests only (separate profile)
mvn test -P performance-tests
```

---

## 8. Deployment Considerations

### Prerequisites

- **Java 11 or higher**: `java -version`
- **Maven 3.6+**: For building project
- **Network access**: Between server and client machines (or localhost)
- **Disk space**: ~1MB for compiled JAR, ~10MB for 10,000 CSV records

### Build & Package

```bash
# Build project
cd vehicle-diagnostics
mvn clean package

# Output: target/vehicle-diagnostics-1.0.0.jar
# Includes all dependencies in fat JAR

# Or build separate modules
mvn clean install
```

### Running the Server

**Start in foreground (for testing)**:
```bash
# From repo root
java -cp target/classes:target/lib/* com.vehiclediag.server.DiagnosticServer

# With custom port
java -cp target/classes:target/lib/* com.vehiclediag.server.DiagnosticServer 5001

# Expected output:
# [INFO] DiagnosticServer started on port 5000
# [INFO] Waiting for client connections...
```

**Start in background (for production)**:
```bash
# Linux/Mac
nohup java -cp target/classes:target/lib/* \
    com.vehiclediag.server.DiagnosticServer > server.log 2>&1 &

# Windows (PowerShell)
Start-Process java -ArgumentList @(
    "-cp", "target\classes;target\lib\*",
    "com.vehiclediag.server.DiagnosticServer"
) -RedirectStandardOutput "server.log"
```

**Shutdown gracefully**:
```bash
# Ctrl+C sends SIGTERM, server closes ServerSocket and shuts down
# Existing client connections finish processing before exit
```

### Running the Client

**Single client session**:
```bash
# From repo root (in different terminal from server)
java -cp target/classes:target/lib/* com.vehiclediag.client.DiagnosticClientApp

# Expected output:
# Connecting to server at localhost:5000...
# Connected!
# 
# ========== Vehicle Diagnostic System ==========
# 1. Diagnose Car
# 2. Diagnose Truck
# 3. Exit
# Select option: 
```

**Multiple concurrent clients** (for testing):
```bash
# Terminal 1: Server
java -cp target/classes:target/lib/* com.vehiclediag.server.DiagnosticServer

# Terminal 2: Client 1
java -cp target/classes:target/lib/* com.vehiclediag.client.DiagnosticClientApp

# Terminal 3: Client 2
java -cp target/classes:target/lib/* com.vehiclediag.client.DiagnosticClientApp

# Terminal 4: Client 3
java -cp target/classes:target/lib/* com.vehiclediag.client.DiagnosticClientApp
```

### Monitoring & Logging

**Log file locations**:
```
logs/diagnostic-server.log          # Server logs
logs/diagnostic-client.log          # Client logs
DiagnosticReports.csv               # Diagnostic data (in server working directory)
```

**View server logs in real-time**:
```bash
tail -f logs/diagnostic-server.log
```

**Sample log output**:
```
[2026-05-03 14:23:45] INFO  DiagnosticServer - Server started on port 5000
[2026-05-03 14:23:51] INFO  ClientHandler - Client connected: localhost:54321
[2026-05-03 14:23:52] INFO  ClientHandler - Request processed: V001 (Car)
[2026-05-03 14:23:52] INFO  ClientHandler - Report persisted to CSV
[2026-05-03 14:23:55] INFO  ClientHandler - Client disconnected: localhost:54321
```

### Troubleshooting Deployment Issues

| Issue | Cause | Solution |
|-------|-------|----------|
| "Address already in use" | Port 5000 in use | Change port: `java ... DiagnosticServer 5001` |
| "Connection refused" | Server not running | Start server first in separate terminal |
| "ClassNotFoundException" | Classpath missing dependencies | Ensure all JARs in target/lib included with `-cp` |
| "CSV file permission denied" | Server lacks write access | Run server from writable directory or check permissions |
| "Thread count too high" | Thread pool exhausted | Limit concurrent clients or increase Constants.MAX_CLIENTS |
| "Memory OutOfMemoryError" | Large CSV or many connections | Increase heap: `java -Xmx2g ...` |

### CSV File Management

**Backup CSV before analysis**:
```bash
cp DiagnosticReports.csv DiagnosticReports.csv.backup-$(date +%Y%m%d)
```

**Archive old CSV and start fresh**:
```bash
mv DiagnosticReports.csv DiagnosticReports.csv.2026-05-03
rm DiagnosticReports.csv  # New file created on first write
```

**View CSV contents**:
```bash
# First 10 rows
head -10 DiagnosticReports.csv

# In Excel/Sheets
open DiagnosticReports.csv  # Mac
start DiagnosticReports.csv # Windows
xdg-open DiagnosticReports.csv # Linux
```

### Performance Tuning

**JVM Heap Size** (if many concurrent connections):
```bash
java -Xms512m -Xmx2g -cp ... com.vehiclediag.server.DiagnosticServer
# -Xms: Initial heap 512MB
# -Xmx: Maximum heap 2GB
```

**Thread Pool Size** (in Constants.java):
```java
public static final int MAX_CLIENTS = 50;  // Increase for more concurrency
```

**Socket Timeout** (prevent hanging clients):
```java
clientSocket.setSoTimeout(30000);  // 30 second timeout
```

### Security Considerations (v1)

**Current limitations**:
- No authentication (assumes trusted local network)
- No encryption (assumes private network)
- No SSL/TLS (suitable for LAN only)

**Future hardening**:
- Add SSL/TLS for remote connections
- Implement client authentication (certificates or tokens)
- Add audit logging with digital signatures
- Encrypt CSV file with sensitive data

---

## Implementation Checklist

Use this checklist to track progress through all phases:

### Phase 0: Setup ✓
- [ ] Maven POM.xml configured
- [ ] Package structure created
- [ ] JUnit 5 + dependencies added
- [ ] Constants.java created with all system constants
- [ ] Project compiles without errors

### Phase 1: Data Models ✓
- [ ] Vehicle abstract class implemented
- [ ] Car and Truck classes implemented
- [ ] DiagnosticRequest class implemented with validation
- [ ] DiagnosticReport class implemented
- [ ] ValidationUtil helper class created
- [ ] All model unit tests passing
- [ ] Serialization round-trip tests passing

### Phase 2: Analysis Engine ✓
- [ ] Analyzer interface defined
- [ ] EngineAnalyzer implemented and tested
- [ ] BatteryAnalyzer implemented and tested
- [ ] FuelAnalyzer implemented and tested
- [ ] FaultAnalyzer implemented and tested
- [ ] DiagnosticProcessor implemented and tested
- [ ] All analyzer unit tests passing (100% branch coverage)

### Phase 3: Server ✓
- [ ] ReportPersistence class implemented (thread-safe CSV writes)
- [ ] ClientHandler class implemented (Runnable, socket handling)
- [ ] DiagnosticServer class implemented (ServerSocket, thread pool)
- [ ] Main.java for server created
- [ ] Server integration tests passing (single client, 3+ concurrent clients)
- [ ] CSV file creation and concurrent write tests passing
- [ ] Client disconnection gracefully handled

### Phase 4: Client ✓
- [ ] ClientUI class implemented (menu system, input prompts)
- [ ] RequestBuilder class implemented
- [ ] DiagnosticClient class implemented (socket connection, request/response)
- [ ] Main.java for client created
- [ ] Client input validation tests passing
- [ ] Client-server integration tests passing

### Phase 5: Integration & E2E ✓
- [ ] End-to-end test suite created
- [ ] Performance tests passing (SC-001, SC-002)
- [ ] Concurrent load tests passing
- [ ] Edge case tests passing
- [ ] CSV data integrity verified across concurrent writes
- [ ] All success criteria verified

### Phase 6: Documentation ✓
- [ ] ARCHITECTURE.md written
- [ ] API_CONTRACTS.md written
- [ ] DEPLOYMENT.md written
- [ ] TROUBLESHOOTING.md written
- [ ] JavaDoc for all public methods
- [ ] Code review completed

---

## Success Criteria Verification Matrix

| SC ID | Requirement | Phase | Verification Method | Status |
|-------|-------------|-------|---------------------|--------|
| SC-001 | Single request < 3 sec | Phase 5 | Performance test | ✓ |
| SC-002 | 5 concurrent requests < 10 sec | Phase 5 | Load test | ✓ |
| SC-003 | CSV 100% data integrity | Phase 5 | Concurrent write test | ✓ |
| SC-004 | Menu < 5 prompts | Phase 4 | Manual testing | ✓ |
| SC-005 | Invalid input 100% rejected | Phase 5 | Edge case test suite | ✓ |
| SC-006 | All exceptions handled | Phase 5 | Exception handling test | ✓ |
| SC-007 | Condition detection 100% accurate | Phase 2 | Analyzer unit tests | ✓ |
| SC-008 | CSV has all fields | Phase 5 | CSV format verification | ✓ |
| SC-009 | Car/Truck polymorphism works | Phase 5 | Integration tests | ✓ |
| SC-010 | Analyzers polymorphic | Phase 2 | Unit tests | ✓ |

---

## References & Additional Resources

### Java Best Practices
- **Effective Java** (Joshua Bloch) - Chapters on threading, serialization
- **Java Concurrency in Practice** (Goetz et al.) - Thread-safe design patterns
- **Clean Code** (Uncle Bob Martin) - Code organization, naming conventions

### Related Technologies
- **Java Socket Programming**: https://docs.oracle.com/javase/tutorial/networking/sockets/
- **Java Serialization**: https://docs.oracle.com/javase/tutorial/jnio/chardet/index.html
- **ExecutorService & Thread Pools**: https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ExecutorService.html
- **ReentrantLock**: https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/locks/ReentrantLock.html
- **Apache Commons CSV**: https://commons.apache.org/proper/commons-csv/

### Logging Framework
- **SLF4J**: https://www.slf4j.org/
- **Log4j2**: https://logging.apache.org/log4j/2.x/

---

## Conclusion

This implementation plan provides a roadmap from empty codebase to production-ready system. By following the phased approach:

1. **Each phase is independently testable** and builds on previous phases
2. **Design decisions are justified** with clear rationale and trade-offs
3. **Risks are identified and mitigated** upfront
4. **Testing strategy ensures quality** at every level
5. **Deployment is straightforward** with clear procedures

The system will be robust, maintainable, and scalable within the defined constraints. Future enhancements (e.g., database persistence, authentication, remote monitoring) can build on this foundation without architectural redesign.

**Estimated Total Implementation Time**: 8-10 business days for 1-2 experienced Java developers, or 12-15 days for junior developers learning concurrency patterns.
