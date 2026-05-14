# Phase 8: SiteTrack Swing Client Implementation Plan

Since building the entire Client Application is a massive undertaking, we have divided Phase 8 into smaller, manageable sub-phases. We will execute these sequentially, testing along the way.

## User Review Required
> [!IMPORTANT]
> Please review this phased approach. If you approve, we will begin executing **Phase 8.1 (Foundation & Authentication)** immediately.

## 1. Dependencies Setup (Manual Action Required)
As you requested, you will manage dependencies manually in NetBeans. 
*   **FlatLaf JAR**: Download the `flatlaf-3.4.jar` (or newer) from Maven Central: [FlatLaf Downloads](https://search.maven.org/artifact/com.formdev/flatlaf) and add it to the libraries of your `SiteTrackClient28279` project.
*   **Server Dependency**: Right-click your Client project in NetBeans -> Properties -> Libraries -> Add Project, and select the `SiteTrackServer28279` project so the client can access the Server's models, DTOs, and RMI interfaces.

---

## 2. Execution Phases

### Phase 8.1: Foundation & Authentication (First Step)
This phase establishes the client's architecture, connects to the server, and secures the app.
*   **Configuration**: Create `config.properties` and `config/RMIConnection.java` for centralized stub lookup.
*   **Session Management**: Create `session/SessionManager.java` to hold the `User` and `ERole` globally.
*   **Entry Point**: Create `Client.java` (Main Class) to initialize FlatLaf and RMI.
*   **Controller**: Create `controller/AuthController.java` to handle RMI calls for login and OTP validation (the server handles dispatching the OTP via RabbitMQ).
*   **Views**:
    *   `view/LoginPanel.java`: UI for email/password and OTP entry.
    *   `view/MainFrame.java`: The core application window with a Sidebar navigation and a `CardLayout` content area.

### Phase 8.2: Role-Based Dashboard & Project Module
Once authenticated, users enter the main system. 
> [!NOTE]
> **Database Check**: No database changes are needed for the two user accounts. The existing `ProjectManager` mapping table perfectly supports Site Managers seeing only their assigned projects, while Admins can simply query the `projects` table for all records.
*   **Controllers**: Create `ProjectController.java`.
*   **Views**:
    *   `view/DashboardPanel.java`: High-level summary statistics.
    *   `view/ProjectPanel.java`: A dynamic view that adjusts its data based on whether `SessionManager.getInstance().getCurrentUser().getRole()` is `ADMIN` or `SITE_MANAGER`.

### Phase 8.3: Materials & Stock Tracking
Implementing the material lifecycle.
*   **Controllers**: Create `MaterialController.java` and `StockController.java`.
*   **Views**:
    *   `view/MaterialPanel.java`: Manage categories and definitions (Admin only).
    *   `view/PurchasePanel.java`: Record new material inflow (Admin).
    *   `view/UsagePanel.java`: Record daily consumption (Site Manager).

### Phase 8.4: Workforce & Payroll Management
Managing daily labor on the ground.
*   **Controllers**: Create `WorkerController.java` and `PayrollController.java`.
*   **Views**:
    *   `view/WorkerPanel.java`: Registering and managing site workers.
    *   `view/AttendancePanel.java`: Logging daily presence per project (Site Manager).
    *   `view/PaymentPanel.java`: Processing daily wage payments, enforcing the prerequisite of a valid attendance record.

---

## Verification Plan
*As requested, manual verification will be handled entirely by you. No automated UI tests will be written for now.*
