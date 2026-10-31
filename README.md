# Meridian People Suite — Enterprise Employee Management System (EMS)

![Java 21](https://img.shields.io/badge/Java-21%20LTS-007396?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.1-6DB33F?logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-RBAC-6DB33F?logo=springsecurity&logoColor=white)
![H2 Database](https://img.shields.io/badge/Database-H2%20%2F%20MySQL-1f6fb5)
![Frontend](https://img.shields.io/badge/Frontend-Vanilla%20ES6%2B%20%7C%20Zero%20Deps-3256d6)

**Meridian People Suite** is a full-stack, enterprise-grade Employee Management System (EMS) engineered with **Java 21 LTS**, **Spring Boot**, **Spring Security (RBAC)**, **Spring Data JPA (Hibernate)**, and an **in-memory H2 Database** (with production MySQL support), paired with a clinical, zero-external-dependency light-theme SaaS frontend.

---

## Key Highlights & Features

- **Role-Based Access Control (RBAC) & Authentication**:
  - Session-backed Spring Security authentication with BCrypt password hashing and `@PreAuthorize` method-level security.
  - Four built-in enterprise roles (**Administrator**, **HR Manager**, **Department Manager**, **Employee**) with a **1-Click Demo Role Switcher** on both the sign-in screen and top navigation bar.
  - Dynamic **compensation privacy**: employee salaries are visible to `ADMIN` and `HR_MANAGER`, while automatically redacted (`Restricted`) for `MANAGER` and `EMPLOYEE` users viewing colleagues.
- **Workforce Directory & Reporting Hierarchy**:
  - Real-time debounced search, multi-attribute filtering (Department, Status), multi-column sorting, and server-side pagination (`Pageable`).
  - Interactive **Organizational Reporting Tree** modal displaying upward managers ("Reports to") and direct reports.
  - Safe **soft-termination workflow** that automatically reassigns direct reports to the next manager up the hierarchy.
- **Leave & Time-Off Desk**:
  - Per-employee annual leave balance tracker (`ANNUAL`, `SICK`, `CASUAL`, `MATERNITY`) with visual utilization bars.
  - Date range validation and automatic **overlapping leave detection**.
  - One-click manager approval/rejection workflow that automatically transitions active employees to `ON_LEAVE` when approved dates cover today.
- **IT Hardware & Asset Registry**:
  - Track company laptops, monitors, access cards, and peripherals (`IN_STOCK`, `ASSIGNED`, `UNDER_REPAIR`).
  - Complete asset checkout, return-to-inventory, and repair lifecycle management.
- **Executive Analytics & Automated Audit Trail**:
  - Real-time KPI summary cards, pure CSS `conic-gradient` workforce status donut chart, and departmental headcount distribution bars.
  - Automatic lifecycle audit event generation (`HIRED`, `PROMOTED`, `TRANSFERRED`, `ROLE_CHANGE`, `STATUS_CHANGE`, `TERMINATED`) whenever employee records change.
- **Resilient Offline Mode (`FALLBACK_STORE`)**:
  - Built-in client-side fallback engine that detects network/server unavailability and seamlessly serves interactive read/write operations from an in-memory store.

---

## Tech Stack & Architecture

| Layer | Technology |
| :--- | :--- |
| **Language & Runtime** | Java 21 LTS (Records, Pattern Matching, `java.time` API) |
| **Backend Framework** | Spring Boot 4.1.1 (Spring Web MVC, Spring Data JPA / Hibernate, Jakarta Bean Validation) |
| **Security** | Spring Security 6 (Session Auth, `BCryptPasswordEncoder`, `@EnableMethodSecurity`, RBAC) |
| **Database** | H2 In-Memory Database (`jdbc:h2:mem:employeedb`) + H2 Web Console (`/h2-console`) & MySQL driver ready |
| **Frontend** | Semantic HTML5, Enterprise CSS3 (Custom Properties, Grid/Flexbox), Vanilla ES6+ JavaScript, Pure Inline SVGs |
| **Testing** | JUnit 5 (Jupiter), Spring Boot Test, `MockMvc`, Spring Security Test |
| **Build Tool** | Apache Maven (`mvnw` / `mvnw.cmd` wrapper included) |

---

## Project Structure

```text
javprod/
├── src/main/java/com/example/javprod/
│   ├── config/          # SecurityConfig, WebConfig, DataInitializer (seeds 5 depts, 13 employees, 4 RBAC users)
│   ├── domain/          # JPA Entities (Employee, Department, LeaveRequest, Asset, EmployeeHistory, AppUser, Role, Permission)
│   ├── dto/             # Java 21 Records for API Requests, Responses, Hierarchy, Analytics, and Auth
│   ├── exception/       # GlobalExceptionHandler (@RestControllerAdvice) & custom domain exceptions
│   ├── repository/      # Spring Data JPA Repositories with custom JPQL & @EntityGraph queries
│   ├── service/         # Transactional business logic & automatic audit trail hooks
│   └── web/             # REST Controllers (Auth, Employee, Department, Leave, Asset, Analytics)
├── src/main/resources/
│   ├── static/          # Meridian People Suite Frontend (login.html, index.html, employees.html, leave.html, assets.html)
│   │   ├── css/         # styles.css (Light-theme SaaS design system)
│   │   └── js/          # api.js (Fetch + Offline Fallback Store) & app.js (UI Engine + RBAC controls)
│   └── application.properties
├── src/test/java/       # End-to-end MockMvc & RBAC integration test suite (13 tests)
├── run.ps1 / run.bat    # One-command Windows startup scripts
├── fix-maven.ps1        # Network diagnostics & Maven cache recovery utility
└── pom.xml
```

---

## Quick Start & How to Run

### Prerequisites
- **Java 21 LTS** installed (`JAVA_HOME` configured, or use the bundled `run.ps1` / `run.bat` scripts).

### Option 1: One-Command Launch (Windows PowerShell / CMD)
```powershell
.\run.ps1
```
*(Or in Command Prompt: `run.bat`)*

### Option 2: Standard Maven Wrapper
```bash
./mvnw spring-boot:run
```

Once started, open your browser to:
- **Application & Sign-In Portal:** [http://localhost:8080/login.html](http://localhost:8080/login.html)
- **Executive Dashboard:** [http://localhost:8080/index.html](http://localhost:8080/index.html)
- **H2 Database Console:** [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
  - **JDBC URL:** `jdbc:h2:mem:employeedb`
  - **User Name:** `sa`
  - **Password:** *(leave empty)*

---

## Pre-Seeded Demo Accounts (RBAC)

Click any demo card on the login page (or use the top-right **Instant Role Switcher** inside the app) to test different permission levels:

| Role | Username | Password | Linked Employee | Access Summary |
| :--- | :--- | :--- | :--- | :--- |
| **Administrator** (`ADMIN`) | `admin` | `admin123` | Marcus Vance | Full system control: workforce CRUD, view all salaries, approve leaves, manage/delete assets & departments. |
| **HR Manager** (`HR_MANAGER`) | `hr` | `hr123` | Elena Rostova | People operations: onboard/edit/terminate employees, view all salaries, approve leaves, assign hardware. |
| **Department Manager** (`MANAGER`) | `manager` | `manager123` | Alexander Wright | Team leadership: read-only directory (colleague salaries restricted), approve/reject leave requests. |
| **Employee** (`EMPLOYEE`) | `employee` | `emp123` | Liam O'Connor | Self-service portal: browse directory (colleague salaries restricted), apply for leave, view assets. |

---

## REST API Endpoints Summary

| Method | Endpoint | Permission Required | Description |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/auth/login` | Public | Authenticate credentials and establish HTTP session |
| `POST` | `/api/auth/logout` | Public | Invalidate session and log out |
| `GET` | `/api/auth/me` | Authenticated | Get current logged-in user profile and permissions |
| `GET` | `/api/analytics/summary` | Authenticated | Executive dashboard KPIs, status breakdown, and audit timeline |
| `GET` | `/api/employees` | Authenticated | Paginated, searchable, sortable employee directory |
| `GET` | `/api/employees/{id}/hierarchy` | Authenticated | Retrieve manager and direct reports hierarchy tree |
| `POST` | `/api/employees` | `EMPLOYEE_WRITE` | Create employee and record `HIRED` audit event |
| `PUT` | `/api/employees/{id}` | `EMPLOYEE_WRITE` | Update employee and record lifecycle audit events |
| `DELETE` | `/api/employees/{id}` | `EMPLOYEE_WRITE` | Soft-terminate employee and reassign direct reports |
| `GET` | `/api/departments` | Authenticated | List all departments with employee counts |
| `POST` | `/api/departments` | `ORG_ADMIN` | Create a new department |
| `GET` | `/api/leaves` | Authenticated | List leave requests filtered by status or employee |
| `POST` | `/api/leaves` | Authenticated | Apply for leave (with overlap validation) |
| `PATCH` | `/api/leaves/{id}/approve` | `LEAVE_DECIDE` | Approve a pending leave request |
| `PATCH` | `/api/leaves/{id}/reject` | `LEAVE_DECIDE` | Reject a pending leave request |
| `GET` | `/api/assets` | Authenticated | List company hardware assets |
| `POST` | `/api/assets` | `ASSET_WRITE` | Register a new hardware asset |
| `POST` | `/api/assets/{id}/checkout` | `ASSET_WRITE` | Assign an in-stock asset to an employee |
| `POST` | `/api/assets/{id}/return` | `ASSET_WRITE` | Return an asset to central `IN_STOCK` inventory |
| `POST` | `/api/assets/{id}/repair` | `ASSET_WRITE` | Mark an asset as `UNDER_REPAIR` |

---

## Running Automated Tests

Run the full JUnit 5 + `MockMvc` integration test suite (covering CRUD, validation, leave workflows, asset lifecycle, analytics, and RBAC security):

```bash
./mvnw clean test
```
