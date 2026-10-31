# Meridian People Suite — Complete Feature & Architecture Guide

**Meridian People Suite** is an enterprise-grade Employee Management System (EMS) built with **Java 21 LTS**, **Spring Boot 4.1.1**, **Spring Security 6**, **Spring Data JPA (Hibernate)**, and an **in-memory H2 Database**, paired with a zero-dependency light-theme SaaS frontend.

---

## 1. Live Verification Status

All modules, REST API endpoints, Role-Based Access Control (RBAC) policies, and UI pages have been verified against the running instance on `http://localhost:8080`:

| Module / Capability | Verification Result | Details |
| :--- | :--- | :--- |
| **Static Frontend & Design System** | `PASSED (200 OK)` | `login.html`, `index.html`, `employees.html`, `leave.html`, `assets.html`, `styles.css`, `api.js`, `app.js` |
| **Unauthenticated API Guard** | `PASSED (401)` | Unauthenticated requests to `/api/**` are rejected with JSON `401 Unauthorized` |
| **Authentication & Session Flow** | `PASSED (200 OK)` | `POST /api/auth/login`, `GET /api/auth/me`, `POST /api/auth/logout`, `GET /api/auth/demo-accounts` |
| **Role-Based Access Control (RBAC)** | `PASSED (403 / 200)` | Method-level `@PreAuthorize` guards + dynamic salary redaction for non-HR/Admin roles |
| **Employee Directory & Org Hierarchy** | `PASSED (200 / 201)` | Paginated search, multi-column sorting, CRUD, soft-termination with report reassignment, org tree |
| **Leave & Time-Off Desk** | `PASSED (200 / 201)` | Overlap detection, annual balance calculation, manager approval/rejection with status transitions |
| **Hardware Asset Registry** | `PASSED (200 / 201)` | Asset registration, employee checkout, return to stock, and repair workflows |
| **Executive Analytics & Audit Trail** | `PASSED (200 OK)` | Headcount KPIs, CSS conic-gradient status donut, department distribution bars, automatic lifecycle timeline |
| **Automated Integration Test Suite** | `PASSED (13/13)` | `EmployeeManagementIntegrationTests` + `JavprodApplicationTests` via `.\mvnw.cmd clean test` |

---

## 2. Authentication & Role-Based Access Control (RBAC)

### 2.1 Pre-Seeded Enterprise Accounts
You can sign in at **`http://localhost:8080/login.html`** using the form, the **1-Click Demo Role Cards** on the login page, or the **Instant Role Switcher** in the top-right user menu of any page:

| Role | Username | Password | Linked Employee | Department |
| :--- | :--- | :--- | :--- | :--- |
| **Administrator** (`ADMIN`) | `admin` | `admin123` | Marcus Vance *(VP of Software Engineering)* | Engineering & Cloud Architecture |
| **HR Manager** (`HR_MANAGER`) | `hr` | `hr123` | Elena Rostova *(Director of People & Culture)* | Human Resources & People Ops |
| **Department Manager** (`MANAGER`) | `manager` | `manager123` | Alexander Wright *(Principal Cloud Architect)* | Engineering & Cloud Architecture |
| **Employee** (`EMPLOYEE`) | `employee` | `emp123` | Liam O'Connor *(DevOps & SRE Engineer)* | Engineering & Cloud Architecture |

### 2.2 Granular Permission Matrix

| Capability / Permission Code | `ADMIN` | `HR_MANAGER` | `MANAGER` | `EMPLOYEE` |
| :--- | :---: | :---: | :---: | :---: |
| **Browse Directory & Org Hierarchy** | Full | Full | Full | Full |
| **Create / Edit / Terminate Employees** (`EMPLOYEE_WRITE`) | Yes | Yes | No *(403)* | No *(403)* |
| **View Colleague Salaries** (`SALARY_VIEW`) | All | All | **Self Only** *(Others Redacted)* | **Self Only** *(Others Redacted)* |
| **Submit Leave Requests** | Yes | Yes | Yes | Yes |
| **Approve / Reject Leave Requests** (`LEAVE_DECIDE`) | Yes | Yes | Yes | No *(403)* |
| **View Hardware Asset Inventory** (`ASSET_VIEW_ALL`) | Yes | Yes | Yes | Yes *(Read-Only)* |
| **Register / Checkout / Return / Repair Assets** (`ASSET_WRITE`) | Yes | Yes | No *(403)* | No *(403)* |
| **Manage Departments & Delete Assets** (`ORG_ADMIN`) | Yes | No *(403)* | No *(403)* | No *(403)* |

---

## 3. Core Functional Modules

### 3.1 Executive Dashboard (`index.html`)
- **Real-Time KPI Cards**: Total headcount (with 90-day recent hire count), active workforce count, pending leave approvals, and total hardware assets (broken down by assigned, in stock, and under repair).
- **Workforce Status Breakdown**: Pure CSS `conic-gradient` donut chart visualizing `ACTIVE`, `ON_LEAVE`, and `TERMINATED` ratios.
- **Departmental Headcount Distribution**: Animated progress bars displaying employee count and percentage share across all 5 corporate departments.
- **Pending Approvals Queue**: Quick-review list of pending leave applications with employee avatars and direct navigation to the Leave Desk.
- **Live Organizational Audit Feed**: Chronological timeline of automated `HIRED`, `PROMOTED`, `TRANSFERRED`, `ROLE_CHANGE`, `STATUS_CHANGE`, and `TERMINATED` events.

### 3.2 People Directory & Org Hierarchy (`employees.html`)
- **Search-as-You-Type & Multi-Filter Bar**: Debounced real-time search across first name, last name, email, and role, combined with Department and Employment Status (`ACTIVE`, `ON_LEAVE`, `TERMINATED`) filters.
- **Sortable & Paginated Table**: Server-side Spring Data JPA pagination (`page`, `size`, `sortBy`, `sortDir`) with keyboard-accessible sortable column headers.
- **Dynamic Salary Privacy**: Compensation is formatted in USD for `ADMIN` and `HR_MANAGER` users, and automatically redacted (`null` from backend, rendered as a `Restricted` lock badge in the UI) when `MANAGER` or `EMPLOYEE` users view other colleagues.
- **Interactive Reporting Hierarchy Modal (`Tree`)**: Displays any employee's reporting manager ("Reports to"), selected profile card, and all direct reports.
- **Lifecycle & Reassignment Logic**:
  - Updating an employee's department, role, salary, or status automatically records a corresponding `EmployeeHistory` audit event.
  - Terminating an employee (`DELETE /api/employees/{id}`) performs a safe soft-termination (`status = TERMINATED`) and automatically reassigns their direct reports to the next manager up the chain.

### 3.3 Leave & Time-Off Desk (`leave.html`)
- **Annual Leave Balance Tracker**: Computes per-employee remaining allowances (`ANNUAL: 20d`, `SICK: 10d`, `CASUAL: 8d`, `MATERNITY: 90d`) with visual utilization bars.
- **Leave Application & Overlap Prevention**: Validates date ranges (`endDate >= startDate`), prevents terminated employees from applying, and rejects overlapping pending/approved leave requests.
- **Manager Approval Workflow**:
  - `ADMIN`, `HR_MANAGER`, and `MANAGER` roles can approve or reject pending requests with one click.
  - Approving a leave that covers the current date automatically transitions the employee's status to `ON_LEAVE` and logs an audit event.

### 3.4 Hardware Asset & IT Registry (`assets.html`)
- **Inventory Filtering**: Filter company hardware by Status (`IN_STOCK`, `ASSIGNED`, `UNDER_REPAIR`) and Category (`LAPTOP`, `MONITOR`, `ACCESS_CARD`, `PERIPHERAL`).
- **Asset Lifecycle Actions** *(restricted to `ADMIN` & `HR_MANAGER`)*:
  - **Register Asset**: Add new hardware with unique Asset Tag and Serial Number, optionally assigning it immediately to an active employee.
  - **Checkout**: Assign an `IN_STOCK` asset to an employee and stamp `assignedDate`.
  - **Return**: Check an `ASSIGNED` or repaired asset back into central `IN_STOCK` inventory.
  - **Repair**: Flag an asset as `UNDER_REPAIR` and unassign it automatically.

### 3.5 Offline Fallback Resilience (`js/api.js`)
- Includes a client-side `FALLBACK_STORE` with a 4-second request timeout detector.
- If the backend is unreachable, the UI displays an `Offline mode` status banner and seamlessly serves read/write operations and RBAC checks from the in-memory store.

---

## 4. REST API Reference

| Method | Endpoint | Access Policy | Description |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/auth/login` | Public | Authenticate username/password and establish HTTP session |
| `POST` | `/api/auth/logout` | Public | Invalidate HTTP session and clear security context |
| `GET` | `/api/auth/me` | Authenticated | Return current user profile, linked employee, and permission set |
| `GET` | `/api/auth/demo-accounts` | Public | Return metadata for the 4 pre-seeded RBAC demo roles |
| `GET` | `/api/analytics/summary` | Authenticated | Return executive KPIs, department distribution, and timeline |
| `GET` | `/api/employees` | Authenticated | Paginated, filtered, sorted directory (redacts salaries by role) |
| `GET` | `/api/employees/{id}/hierarchy` | Authenticated | Return manager, employee, and direct reports tree |
| `POST` | `/api/employees` | `EMPLOYEE_WRITE` | Onboard new employee and log `HIRED` audit event |
| `PUT` | `/api/employees/{id}` | `EMPLOYEE_WRITE` | Update employee and emit lifecycle audit events |
| `DELETE` | `/api/employees/{id}` | `EMPLOYEE_WRITE` | Soft-terminate employee and reassign direct reports |
| `GET` | `/api/departments` | Authenticated | List all departments with headcount totals |
| `POST` | `/api/departments` | `ORG_ADMIN` | Create a new department |
| `GET` | `/api/leaves` | Authenticated | List leave requests filtered by `status` or `employeeId` |
| `POST` | `/api/leaves` | Authenticated | Submit a new leave application with overlap validation |
| `PATCH` | `/api/leaves/{id}/approve` | `LEAVE_DECIDE` | Approve a pending leave request |
| `PATCH` | `/api/leaves/{id}/reject` | `LEAVE_DECIDE` | Reject a pending leave request |
| `GET` | `/api/assets` | Authenticated | List hardware assets filtered by `status` or `category` |
| `POST` | `/api/assets` | `ASSET_WRITE` | Register a new hardware asset |
| `POST` | `/api/assets/{id}/checkout` | `ASSET_WRITE` | Assign an in-stock asset to an employee |
| `POST` | `/api/assets/{id}/return` | `ASSET_WRITE` | Return an asset to central `IN_STOCK` inventory |
| `POST` | `/api/assets/{id}/repair` | `ASSET_WRITE` | Transition an asset to `UNDER_REPAIR` |

---

## 5. How to Run & Test

### Start the Application
From `C:\javprod`, run the included PowerShell launcher script:
```powershell
.\run.ps1
```
*(Or in Command Prompt: `run.bat`)*. Then open **`http://localhost:8080/login.html`**.

### Run the Automated Test Suite
```powershell
$env:JAVA_HOME = "C:\java\OpenJDK21U-jdk_x64_windows_hotspot_21.0.12.1_1\jdk-21.0.12.1+1"
$env:Path = "$env:JAVA_HOME\bin;$env:Path"
.\mvnw.cmd clean test
```
