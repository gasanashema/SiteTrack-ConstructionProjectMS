# SiteTrack Construction Manager

## Overview
SiteTrack Construction Manager is a distributed construction site management system designed to track and manage materials and labor across multiple active projects.

### Key Features
1. **Materials Tracking**: Manages purchasing, stock per site, daily usage, and maintains a strict audit trail of all stock movements.
2. **Labor (Umubyizi) Tracking**: Tracks daily worker attendance per project and manages daily wage payments.

### User Roles
- **Admin**: Full access to all modules, including users, projects, materials, workers, and cross-site reports.
- **Site Manager**: Restricted access, limited to assigned projects. Responsible for recording daily material usage, worker attendance, and processing wage payments.

## Technology Stack
- **Communication Layer**: Java RMI (Strictly no REST, sockets, or HTTP)
- **Client UI**: Java Swing (JFrame-based forms)
- **Database & ORM**: PostgreSQL (`SiteTrackConstructionManager`) with Hibernate (No raw JDBC queries)
- **Message Broker**: ActiveMQ or RabbitMQ (for OTP delivery and low stock alerts)
- **Design Patterns**: MVC (Model-View-Controller) and DAO (Data Access Object)

## Application Architecture
The system is divided into two separate applications to enforce strict security and separation of concerns:

1. **Server Application (`SiteTrackServer28279`)**
   - Handles all business logic in the service layer.
   - Manages database persistence via Hibernate and DAO interfaces.
   - Exposes services to the client via Java RMI on a port between 3000 and 6000.
   - Integrates with the message broker.
   - Enforces role-based access control.

2. **Client Application (`SiteTrackClient28279`)**
   - Manages the graphical user interface using Java Swing.
   - Communicates with the server entirely via Java RMI remote method calls.
   - **Zero direct database access.**

## Database Structure & ID Generation
The database relies on PostgreSQL sequences with a custom ID generation strategy. Primary keys are meaningful strings formatted as `PREFIX-000` (e.g., `MAT-001`, `USR-001`). ID generation occurs programmatically in the Java service layer prior to persistence.

### Entities
1. `users` (USR)
2. `projects` (PRJ)
3. `project_managers` (PMG)
4. `material_categories` (MCA)
5. `materials` (MAT)
6. `material_purchases` (MPU)
7. `project_material_stock` (STK)
8. `material_stock_movements` (MSM)
9. `material_usage` (MUS)
10. `project_activities` (ACT)
11. `worker_types` (WKT)
12. `site_workers` (WKR)
13. `worker_attendance` (ATT)
14. `worker_payments` (PAY)
15. `otp_verifications` (OTP)
16. `notification_logs` (NTF)

## Critical Business Rules
1. **Material Purchasing**: Recording a purchase increases project stock, recalculates the average unit price using a weighted average formula, and logs a traceable "IN" movement.
2. **Material Usage**: Recording usage decreases project stock and logs an "OUT" movement. The operation is strictly rejected if usage exceeds the available stock.
3. **Stock Adjustments**: Manual stock adjustments correct the available quantity and log an "ADJUSTMENT" movement to maintain the audit trail.
4. **Worker Payments**: Worker payments can only be processed if there is a verified attendance record marking the worker as 'PRESENT' for that exact date and project.
5. **Low Stock Alerts**: Automatically triggers a message broker notification when a material's stock falls below its defined minimum threshold.
