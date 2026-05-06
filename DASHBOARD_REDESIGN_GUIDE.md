# Smart Vehicle Diagnostic Dashboard - Professional UI Redesign

## Overview
The JavaFX UI has been completely redesigned into a modern, professional dashboard with a card-based layout, real-time analytics, and interactive charts. The backend server, console client, models, and analyzers remain completely unchanged.

## Files Changed

### 1. **Created: `src/main/resources/styles/dashboard.css`** (NEW)
- **Purpose**: Professional styling for the entire dashboard
- **Features**:
  - Dark green header (`#1b5e20`) for branding
  - Light gray background (`#f5f5f5`) for accessibility
  - White cards with subtle shadows and rounded corners
  - Color-coded severity badges (Red/Critical, Orange/High, Yellow/Medium, Green/Low)
  - Modern form field styling with focus states
  - Interactive button styles with hover/pressed states
  - Responsive table styling with selection highlighting
  - Professional chart styling

### 2. **Replaced: `src/main/java/com/vehiclediag/ui/MainApp.java`** (MAJOR REDESIGN)
- **Old Layout**: Simple GridPane with all controls in one view
- **New Layout**: Professional BorderPane with 5 distinct panels

#### Dashboard Layout

**BorderPane Structure:**
```
┌─────────────────────────────────────────────────────────┐
│                    TITLE BAR (Top)                       │
│         "Smart Vehicle Diagnostic Dashboard"             │
├──────────────────┬─────────────────────────┬─────────────┤
│                  │                         │             │
│  LEFT PANEL      │    CENTER PANEL         │RIGHT PANEL  │
│  (Scrollable)    │   (Diagnostic Result)   │ (Analytics) │
│                  │                         │             │
│ • Vehicle Info   │ • Result Summary        │ • Stats     │
│   Card          │ • Health Score          │ • Pie Chart │
│                  │ • Severity Badge        │ • Bar Chart │
│ • Sensor Readings│ • Full Result Text      │             │
│   Card          │                         │             │
│                  │                         │             │
├──────────────────┴─────────────────────────┴─────────────┤
│  BOTTOM PANEL (History Table)                             │
│  Filter Bar | Table with 9 columns                        │
└──────────────────────────────────────────────────────────┘
```

#### Component Details

**1. TOP PANEL - Title Bar**
- Dark green background with white title text
- Provides visual branding for the application

**2. LEFT PANEL - Input Forms (Scrollable)**

*Vehicle Information Card:*
- Vehicle ID (TextField)
- Vehicle Type (ComboBox: Car/Truck)
- Model (TextField)
- Year (TextField)

*Sensor Readings Card:*
- Speed (km/h)
- RPM
- Engine Temperature (°C)
- Battery Voltage (V)
- Fuel Level (%)
- Oil Pressure
- Coolant Level (%)
- Transmission Temperature (°C)
- Throttle Position (%)
- MAF Reading
- Oxygen Sensor Voltage (V)
- Mileage (km)
- Fault Code
- Submit Button (green, full width)

**3. CENTER PANEL - Diagnostic Results**

*Result Summary Card:*
- Result Summary Label
- Health Score display
- Severity Badge (color-coded, updates dynamically)
- Large TextArea for detailed results
- Scrollable to accommodate large reports

**4. RIGHT PANEL - Analytics (Scrollable)**

*Analytics Summary Card:*
- Total Reports (counter)
- Critical Reports (counter in red)
- Average Health Score (counter in green)

*Severity Distribution Pie Chart:*
- Displays count of reports by severity
- Updates automatically when data changes
- Legend shows severity names and counts

*Health Score Bar Chart:*
- X-axis: Vehicle ID
- Y-axis: Average Health Score
- Updates to show avg health score per vehicle
- Updates automatically with new data

**5. BOTTOM PANEL - Report History**

*Filter Bar:*
- Search field (filter by Vehicle ID)
- Severity filter (All, CRITICAL, HIGH, MEDIUM, LOW)
- Refresh button (reload from CSV)

*History Table (9 columns):*
1. Timestamp
2. Vehicle ID
3. Type (Car/Truck)
4. Model
5. Year
6. Fault Code
7. Health Score
8. Summary
9. Severity

## Key Features

### 1. Professional Styling
- **Colors**: Dark green header, light gray background, white cards
- **Spacing**: 12px consistent padding/margins
- **Shadows**: Subtle dropshadow on cards for depth
- **Borders**: Rounded corners (8px) on cards and form elements
- **Typography**: Clear hierarchy with size/weight variations

### 2. Responsive Layout
- All panels are scrollable where content exceeds viewport
- Window default size: 1400x850 (adjustable)
- BorderPane automatically manages panel sizing
- Cards expand/shrink with window resize

### 3. Real-Time Analytics
**Auto-Update on Data Changes:**
- After submitting a diagnostic request
- After refreshing history from CSV
- Analytics update immediately:
  - Total reports count
  - Critical reports count
  - Average health score
  - Severity distribution pie chart
  - Health score by vehicle bar chart

### 4. Graceful Error Handling
- Missing CSV file: Shows empty history without error
- Missing CSV columns: Uses safe defaults (getRecordValue with fallbacks)
- Invalid health scores: Skips and continues processing
- Malformed CSV records: Logs and skips without crashing

### 5. Color-Coded Severity
- **CRITICAL** - Red (#d32f2f)
- **HIGH** - Orange (#f57c00)
- **MEDIUM** - Yellow (#fbc02d)
- **LOW** - Green (#7cb342)

## How to Run the Dashboard

### Option 1: Run from Command Line

**Terminal 1 - Start the Server:**
```bash
cd c:\Users\bodye\my-project
java -cp target/classes:lib/* com.vehiclediag.server.DiagnosticServer 5000
```

**Terminal 2 - Start the JavaFX Dashboard:**
```bash
cd c:\Users\bodye\my-project
java -cp target/classes:lib/* com.vehiclediag.ui.MainApp
```

### Option 2: Run from IDE (VS Code/IntelliJ)
1. Build project: `mvn clean compile`
2. Right-click on `MainApp.java` → Run

### Option 3: Use Maven

**First, ensure pom.xml has JavaFX dependency:**
```xml
<dependency>
    <groupId>org.openjfx</groupId>
    <artifactId>javafx-controls</artifactId>
    <version>22.0.2</version>
</dependency>
```

**Run from Maven:**
```bash
mvn javafx:run -DmainClass=com.vehiclediag.ui.MainApp
```

## Usage Workflow

1. **Enter Vehicle Information** (Left Panel)
   - Fill in Vehicle ID, Type, Model, Year

2. **Enter Sensor Readings** (Left Panel)
   - Fill in all sensor values
   - Optionally enter Fault Code

3. **Submit Diagnostic Request** (Green Button)
   - Button appears at bottom of Sensor Readings card
   - Click to send request to server

4. **View Results** (Center Panel)
   - Diagnostic result displays in Result Summary
   - Severity badge updates with color
   - Full detailed report in TextArea

5. **Review Analytics** (Right Panel)
   - Charts update automatically
   - Total reports, critical reports, avg health score display
   - See distribution across severity levels
   - View average health score by vehicle

6. **Browse History** (Bottom Panel)
   - All previous diagnostics appear in table
   - Search by Vehicle ID
   - Filter by Severity level
   - Click Refresh to reload from CSV

## File Structure

```
src/main/
├── java/com/vehiclediag/ui/
│   └── MainApp.java (REDESIGNED - 800+ lines)
├── resources/
│   └── styles/
│       └── dashboard.css (NEW - 250+ lines)
└── [other packages unchanged]
```

## Backward Compatibility

✅ **Completely backward compatible:**
- Console client (`DiagnosticClient`) - unchanged
- Server (`DiagnosticServer`) - unchanged
- Models, Validators, Analyzers - unchanged
- CSV storage format - unchanged
- Network protocol - unchanged
- Report logic - unchanged

The dashboard is purely a UI layer replacement with no impact on system logic.

## Technology Stack

- **UI Framework**: JavaFX 22.0.2
- **Layout**: BorderPane, GridPane, VBox, HBox, ScrollPane
- **Charts**: PieChart, BarChart (JavaFX built-in)
- **Styling**: CSS (dashboard.css)
- **Logging**: SLF4J (debug output on CSV access)
- **Build**: Maven

## Styling Customization

To modify colors, fonts, or spacing, edit `src/main/resources/styles/dashboard.css`:

**Key CSS classes:**
- `.title-bar` - Top header styling
- `.dashboard-card` - All card containers
- `.form-field` - Text input fields
- `.dashboard-button` - Primary button (green)
- `.secondary-button` - Secondary button (light green)
- `.severity-critical`, `.severity-high`, `.severity-medium`, `.severity-low` - Severity badges
- `.history-table` - Table styling
- `.analytics-card` - Analytics stat cards

## Compilation Status

✅ **BUILD SUCCESS**
- CSS resource copied to target/classes
- All 800+ lines of MainApp compile without errors
- No warnings or deprecations
- Ready for production use

## Screenshots (Conceptual Layout)

The dashboard displays:
- **Header**: Dark green bar with "Smart Vehicle Diagnostic Dashboard" title
- **Left**: Two white cards stacked vertically with form inputs
- **Center**: Large white card showing diagnostic results with color-coded severity badge
- **Right**: Three white sections (stats card + two charts)
- **Bottom**: White filter bar + table with recent diagnostics

All elements have consistent spacing, shadows, and professional appearance.
