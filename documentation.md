# SiteTrack Construction Manager Documentation

## Phase 1: Core Concepts & Architecture

### 1. What is SiteTrack?
SiteTrack Construction Manager is a robust, distributed software system designed specifically to handle the complexities of managing multiple active construction sites simultaneously. It acts as a centralized brain for a construction company, focusing on tracking two critical resources with absolute precision:
*   **Materials**: The system tracks the entire lifecycle of construction materials. This includes recording initial purchases, maintaining real-time stock levels for each specific project site, logging daily material consumption, and—most importantly—enforcing a strict, unbroken audit trail of every single stock movement (in, out, or manual adjustment).
*   **Labor (Umubyizi)**: The system handles the daily workforce on the ground. It tracks daily worker attendance on a per-project basis and processes their daily wage payments tied directly to verified attendance.

### 2. User Roles & Access Control
The system employs strict Role-Based Access Control (RBAC) to ensure data integrity and operational security. There are two primary roles:
*   **Admin**: The master role. Admins have unrestricted, global access to all modules across the entire system. They can manage all users, create projects, define materials, oversee all site workers, and generate comprehensive cross-site reports.
*   **Site Manager**: A localized role. Site Managers are securely restricted to interacting only with the specific projects they have been explicitly assigned to. Their primary responsibilities are data entry on the ground: recording daily material usage, logging worker attendance, and processing daily wage payments for their assigned sites.

### 3. The Technology Stack
The application enforces a rigid technology stack to ensure stability and maintain a specific architectural pattern:
*   **Communication Layer**: **Java RMI** (Remote Method Invocation) is strictly the *only* allowed communication protocol between the client and the server. No REST APIs, no WebSockets, and no HTTP.
*   **Client Interface**: **Java Swing**. The entire graphical user interface is built using native Java Swing JFrame forms and panels.
*   **Database & ORM**: **PostgreSQL** (Database name: `SiteTrackConstructionManager`) paired with **Hibernate** as the Object-Relational Mapper. Raw JDBC queries are strictly prohibited; all database interactions must pass through Hibernate.
*   **Message Broker**: **ActiveMQ or RabbitMQ**. This handles asynchronous notification events, specifically delivering OTP codes during login and broadcasting low-stock alerts.

### 4. The Two Separate Applications
To physically enforce separation of concerns and security, the system is split into two distinct Maven-based applications:
1.  **Server Application (`SiteTrackServer28279`)**: The brain of the system. It houses all the business logic, manages database persistence via Hibernate DAOs, integrates with the message broker, enforces security, and exposes its functions via RMI on a specific port (between 3000 and 6000).
2.  **Client Application (`SiteTrackClient28279`)**: The user-facing shell. It manages the Swing GUI and connects to the server purely through RMI remote stubs. **It has zero direct connection or access to the database.**

### 5. Guiding Design Principles
These are the non-negotiable rules of the system:
*   **Absolute Separation**: The client application must NEVER touch the database. All data flows through RMI service methods.
*   **Centralized Logic**: All business calculations, validations, and logic reside strictly on the server side in the service layer.
*   **Server-Side Security**: Role-based access control is evaluated and enforced by the server, not the client.
*   **2FA Requirement**: Every user login must be authenticated with a One-Time Password (OTP) before any system features can be accessed.
*   **Strict Prerequisite Validation**: 
    *   A worker cannot be paid unless there is an existing, verified attendance record marking them as `PRESENT` for that exact date and project.
    *   Material usage cannot be recorded if the requested quantity exceeds the currently available stock.
*   **Mandatory Auditing**: Every single change to stock levels (whether a purchase, usage, or manual adjustment) must automatically generate a corresponding audit record in the `material_stock_movements` table.

### 6. Design Patterns
*   **MVC (Model-View-Controller)**: The core pattern shaping the system. Models (Entities) and Controllers (Services) live on the server, while Views (Swing Forms) and UI Controllers live on the client.
*   **DAO (Data Access Object)**: Every single database entity has a corresponding DAO interface and implementation class on the server to handle database CRUD operations.
*   **RMI Design**: Service interfaces extend `java.rmi.Remote` while implementations extend `UnicastRemoteObject`.

### 7. RMI Server Port
The server operates its RMI registry on a port specified between **3000 and 6000**.

---

## Phase 2: Database Structure & Relationships

### 1. Database Overview
*   **DBMS**: PostgreSQL
*   **Database Name**: `SiteTrackConstructionManager`
*   **ID Strategy**: Primary keys are human-readable `VARCHAR` strings formatted as `PREFIX-000` (e.g., `MAT-001`). These are generated server-side using PostgreSQL sequences before saving via Hibernate. `@GeneratedValue` is **not** used.

### 2. Tables and Prefixes
The system consists of 16 core tables, each tied to a specific sequence (e.g., `seq_users_id`):
1.  **`users`** | Prefix: `USR` | Example: `USR-001`
2.  **`projects`** | Prefix: `PRJ` | Example: `PRJ-001`
3.  **`project_managers`** | Prefix: `PMG` | Example: `PMG-001`
4.  **`material_categories`** | Prefix: `MCA` | Example: `MCA-001`
5.  **`materials`** | Prefix: `MAT` | Example: `MAT-001`
6.  **`material_purchases`** | Prefix: `MPU` | Example: `MPU-001`
7.  **`project_material_stock`** | Prefix: `STK` | Example: `STK-001`
8.  **`material_stock_movements`** | Prefix: `MSM` | Example: `MSM-001`
9.  **`material_usage`** | Prefix: `MUS` | Example: `MUS-001`
10. **`project_activities`** | Prefix: `ACT` | Example: `ACT-001`
11. **`worker_types`** | Prefix: `WKT` | Example: `WKT-001`
12. **`site_workers`** | Prefix: `WKR` | Example: `WKR-001`
13. **`worker_attendance`** | Prefix: `ATT` | Example: `ATT-001`
14. **`worker_payments`** | Prefix: `PAY` | Example: `PAY-001`
15. **`otp_verifications`** | Prefix: `OTP` | Example: `OTP-001`
16. **`notification_logs`** | Prefix: `NTF` | Example: `NTF-001`

### 3. Key Relationships
*   **One-to-Many**: `users` → `projects`. A single user (Admin) can create multiple projects.
*   **Many-to-Many**: `projects` ↔ `users` (Site Managers). A project can have multiple site managers, and a manager can be assigned to multiple projects. This is structurally resolved using the `project_managers` join table.
*   **One-to-One**: `worker_attendance` ↔ `worker_payments`. A single daily attendance record links to exactly one wage payment, enforced via the `UNIQUE(attendance_id)` constraint on the payments table.

### 4. Critical Business Logic Tied to the Database
The structural integrity of the application relies on 5 fundamental rules evaluated directly during database operations:

1.  **Material Inflow (Purchasing)**:
    *   Recording a purchase adds to `quantity_available` in the project's stock.
    *   It recalculates the `average_unit_price` using a weighted average formula.
    *   It generates an `IN` movement record in `material_stock_movements` referencing the purchase ID.
2.  **Material Outflow (Usage)**:
    *   Recording usage subtracts from `quantity_available` in the project's stock.
    *   It generates an `OUT` movement record referencing the usage ID.
    *   *Constraint*: The operation is completely **REJECTED** if the requested quantity exceeds the available stock.
3.  **Stock Auditing (Adjustments)**:
    *   Manual corrections directly update the `quantity_available`.
    *   An `ADJUSTMENT` movement record is immediately created to maintain the audit trail.
4.  **Prerequisite for Labor Payments**:
    *   Before a payment is processed, the system verifies an attendance record exists for that specific worker, project, and date, strictly marked as `PRESENT`.
    *   *Constraint*: Without this `PRESENT` record, the payment is **REJECTED**. The resulting payment must directly reference this attendance ID.
5.  **Automated Stock Alerts**:
    *   When a material's stock dips below its `minimum_quantity` threshold, a notification event is immediately dispatched to the message broker.
    *   A record is created in `notification_logs` with a status of `PENDING` until delivery success is confirmed.

---

## Phase 3: Project Structure, Packages & Hibernate Entity Mapping Plan

### 1. Applications & Root Packages
*   **Server App**: `SiteTrackServer28279`
    *   **Root Package**: `com.sitetrack.server`
*   **Client App**: `SiteTrackClient28279`
    *   **Root Package**: `com.sitetrack.client`

### 2. Four Main Layers of SiteTrackServer28279
The backend application is strictly organized into four logical layers:
1.  **`entity`**: Contains all 16 Hibernate POJOs. These classes represent the database tables directly and include relationship annotations (`@OneToMany`, `@ManyToOne`, etc.).
2.  **`dao`**: Contains the Data Access Object pattern interfaces and their corresponding implementations. This layer handles all raw database CRUD operations using the Hibernate Session.
3.  **`service`**: The core layer for business logic. 
    *   It contains interfaces extending `java.rmi.Remote`.
    *   It contains implementations extending `UnicastRemoteObject`.
    *   It handles complex operations (like adjusting stock averages, validating payment prerequisites) before saving.
4.  **`util`**: Contains critical utilities:
    *   `HibernateUtil.java`: A singleton that configures and holds the central `SessionFactory`.
    *   `IdGeneratorUtil.java`: The utility responsible for custom primary key generation.

### 3. Custom ID Generation Mechanism (`IdGeneratorUtil`)
To generate IDs like `MAT-001`, the system entirely bypasses standard JPA `@GeneratedValue` annotations. Instead:
1.  Before the service layer saves a new entity, it calls a static generation method.
2.  `IdGeneratorUtil` opens a Hibernate session and executes a native PostgreSQL sequence query (e.g., `SELECT nextval('seq_materials_id')`).
3.  It retrieves the generated `long` value.
4.  It formats the string by appending the specific prefix, a hyphen, and zero-padding the number to three digits using `String.format("%03d", value)`.
5.  This string is then manually assigned to the entity's `@Id` field.

### 4. Client-Server Communication Strategy
*   **Zero Database Dependency**: The client application (`SiteTrackClient28279`) does not contain Hibernate, database drivers, or any connection strings. It cannot talk to PostgreSQL directly under any circumstances.
*   **Java RMI**: All interaction occurs over Remote Method Invocation. The server publishes its service implementations to an RMI registry. The client looks up these services using `Naming.lookup()`.
*   **Centralized Connection Pooling**: The client uses an `RMIConnection.java` class to establish the lookup once and cache the remote stubs. Client UI controllers simply invoke methods on these stubs to fetch or push data.

### 5. Notification Events & The Message Broker
The system relies on an asynchronous message broker (ActiveMQ/RabbitMQ) configured on the server side to handle two specific real-time events:
1.  **OTP Delivery**: Triggered on every user login attempt to dispatch the 6-digit 2FA code.
2.  **Low Stock Alerts**: Automatically triggered when a material's stock level dips below its designated minimum threshold during usage or adjustments.

**Broker Workflow**:
*   A `NotificationProducer` packages the event and sends it to a message queue.
*   A `NotificationConsumer` continuously listens to this queue, processes incoming events asynchronously, and updates the `notification_logs` table (moving status from `PENDING` to `SENT` or `FAILED`).
*   *Fail-safe Rule*: If the message broker is temporarily down, the application must catch the failure gracefully and **must not block** critical paths like user login.
