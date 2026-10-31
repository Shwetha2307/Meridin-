package com.example.javprod.config;

import com.example.javprod.domain.*;
import com.example.javprod.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final LeaveRequestRepository leaveRequestRepository;
    private final AssetRepository assetRepository;
    private final EmployeeHistoryRepository historyRepository;
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        if (departmentRepository.count() > 0) {
            log.info("Database already initialized. Skipping seeding.");
            return;
        }

        log.info("Seeding Enterprise Employee Management System database with initial data...");

        // 1. Departments
        Department eng = departmentRepository.save(Department.builder()
            .name("Engineering & Cloud Architecture")
            .code("ENG")
            .description("Core distributed systems, cloud infrastructure, AI models, and platform reliability.")
            .build());

        Department hr = departmentRepository.save(Department.builder()
            .name("Human Resources & People Ops")
            .code("HR")
            .description("Talent acquisition, organizational development, total rewards, and culture.")
            .build());

        Department fin = departmentRepository.save(Department.builder()
            .name("Corporate Finance & Treasury")
            .code("FIN")
            .description("Corporate financial planning, fiscal audits, payroll, and investor relations.")
            .build());

        Department prd = departmentRepository.save(Department.builder()
            .name("Product Strategy & Design")
            .code("PRD")
            .description("User experience research, system interfaces, product roadmaps, and analytics.")
            .build());

        Department mkt = departmentRepository.save(Department.builder()
            .name("Global Sales & Growth")
            .code("MKT")
            .description("Enterprise solution sales, brand awareness, client partnerships, and demand gen.")
            .build());

        // 2. Executive Leadership
        Employee marcus = employeeRepository.save(Employee.builder()
            .firstName("Marcus")
            .lastName("Vance")
            .email("marcus.vance@enterprise.io")
            .phone("+1 (555) 019-2831")
            .role("VP of Software Engineering")
            .hireDate(LocalDate.now().minusYears(4).minusMonths(2))
            .salary(new BigDecimal("210000.00"))
            .status(EmployeeStatus.ACTIVE)
            .department(eng)
            .manager(null)
            .build());

        Employee elena = employeeRepository.save(Employee.builder()
            .firstName("Elena")
            .lastName("Rostova")
            .email("elena.rostova@enterprise.io")
            .phone("+1 (555) 018-9942")
            .role("Director of People & Culture")
            .hireDate(LocalDate.now().minusYears(3).minusMonths(6))
            .salary(new BigDecimal("175000.00"))
            .status(EmployeeStatus.ACTIVE)
            .department(hr)
            .manager(null)
            .build());

        Employee david = employeeRepository.save(Employee.builder()
            .firstName("David")
            .lastName("Sterling")
            .email("david.sterling@enterprise.io")
            .phone("+1 (555) 014-5521")
            .role("Chief Financial Officer")
            .hireDate(LocalDate.now().minusYears(5))
            .salary(new BigDecimal("225000.00"))
            .status(EmployeeStatus.ACTIVE)
            .department(fin)
            .manager(null)
            .build());

        Employee sophia = employeeRepository.save(Employee.builder()
            .firstName("Sophia")
            .lastName("Lin")
            .email("sophia.lin@enterprise.io")
            .phone("+1 (555) 017-3319")
            .role("VP of Product Experience")
            .hireDate(LocalDate.now().minusYears(2).minusMonths(8))
            .salary(new BigDecimal("195000.00"))
            .status(EmployeeStatus.ACTIVE)
            .department(prd)
            .manager(null)
            .build());

        Employee carlos = employeeRepository.save(Employee.builder()
            .firstName("Carlos")
            .lastName("Mendoza")
            .email("carlos.mendoza@enterprise.io")
            .phone("+1 (555) 012-7744")
            .role("VP of Enterprise Sales")
            .hireDate(LocalDate.now().minusYears(3).minusMonths(1))
            .salary(new BigDecimal("185000.00"))
            .status(EmployeeStatus.ACTIVE)
            .department(mkt)
            .manager(null)
            .build());

        // 3. Senior & Staff Staff
        Employee alex = employeeRepository.save(Employee.builder()
            .firstName("Alexander")
            .lastName("Wright")
            .email("alexander.wright@enterprise.io")
            .phone("+1 (555) 020-8812")
            .role("Principal Cloud Architect")
            .hireDate(LocalDate.now().minusYears(2).minusMonths(4))
            .salary(new BigDecimal("172000.00"))
            .status(EmployeeStatus.ACTIVE)
            .department(eng)
            .manager(marcus)
            .build());

        Employee maya = employeeRepository.save(Employee.builder()
            .firstName("Maya")
            .lastName("Patel")
            .email("maya.patel@enterprise.io")
            .phone("+1 (555) 021-9934")
            .role("Senior Staff Security Engineer")
            .hireDate(LocalDate.now().minusYears(1).minusMonths(10))
            .salary(new BigDecimal("164000.00"))
            .status(EmployeeStatus.ACTIVE)
            .department(eng)
            .manager(marcus)
            .build());

        Employee lucas = employeeRepository.save(Employee.builder()
            .firstName("Lucas")
            .lastName("Bernard")
            .email("lucas.bernard@enterprise.io")
            .phone("+1 (555) 023-4411")
            .role("Senior Full-Stack Engineer")
            .hireDate(LocalDate.now().minusYears(1).minusMonths(3))
            .salary(new BigDecimal("132000.00"))
            .status(EmployeeStatus.ON_LEAVE)
            .department(eng)
            .manager(alex)
            .build());

        Employee chloe = employeeRepository.save(Employee.builder()
            .firstName("Chloe")
            .lastName("Dubois")
            .email("chloe.dubois@enterprise.io")
            .phone("+1 (555) 025-6677")
            .role("Senior People Partner")
            .hireDate(LocalDate.now().minusMonths(14))
            .salary(new BigDecimal("118000.00"))
            .status(EmployeeStatus.ACTIVE)
            .department(hr)
            .manager(elena)
            .build());

        Employee nathan = employeeRepository.save(Employee.builder()
            .firstName("Nathan")
            .lastName("Drake")
            .email("nathan.drake@enterprise.io")
            .phone("+1 (555) 026-1188")
            .role("Lead Financial Analyst")
            .hireDate(LocalDate.now().minusMonths(18))
            .salary(new BigDecimal("138000.00"))
            .status(EmployeeStatus.ACTIVE)
            .department(fin)
            .manager(david)
            .build());

        Employee aisha = employeeRepository.save(Employee.builder()
            .firstName("Aisha")
            .lastName("Al-Mansoor")
            .email("aisha.mansoor@enterprise.io")
            .phone("+1 (555) 028-5522")
            .role("Lead Design Systems Specialist")
            .hireDate(LocalDate.now().minusMonths(8))
            .salary(new BigDecimal("152000.00"))
            .status(EmployeeStatus.ACTIVE)
            .department(prd)
            .manager(sophia)
            .build());

        Employee ryan = employeeRepository.save(Employee.builder()
            .firstName("Ryan")
            .lastName("Gallagher")
            .email("ryan.gallagher@enterprise.io")
            .phone("+1 (555) 029-7711")
            .role("Senior Account Executive")
            .hireDate(LocalDate.now().minusMonths(5))
            .salary(new BigDecimal("142000.00"))
            .status(EmployeeStatus.ACTIVE)
            .department(mkt)
            .manager(carlos)
            .build());

        Employee liam = employeeRepository.save(Employee.builder()
            .firstName("Liam")
            .lastName("O'Connor")
            .email("liam.oconnor@enterprise.io")
            .phone("+1 (555) 031-8844")
            .role("DevOps & SRE Engineer")
            .hireDate(LocalDate.now().minusDays(25)) // Recent Hire
            .salary(new BigDecimal("98000.00"))
            .status(EmployeeStatus.ACTIVE)
            .department(eng)
            .manager(alex)
            .build());

        // 4. Seed Audit History
        historyRepository.saveAll(List.of(
            EmployeeHistory.builder().employee(marcus).eventType(AuditEventType.HIRED).description("Executive appointed as VP of Software Engineering").timestamp(LocalDateTime.now().minusYears(4)).build(),
            EmployeeHistory.builder().employee(alex).eventType(AuditEventType.HIRED).description("Joined Engineering team as Senior Cloud Architect").timestamp(LocalDateTime.now().minusYears(2).minusMonths(4)).build(),
            EmployeeHistory.builder().employee(alex).eventType(AuditEventType.PROMOTED).description("Promoted to Principal Cloud Architect; compensation elevated to $172,000").timestamp(LocalDateTime.now().minusMonths(6)).build(),
            EmployeeHistory.builder().employee(lucas).eventType(AuditEventType.HIRED).description("Joined platform engineering squad").timestamp(LocalDateTime.now().minusYears(1).minusMonths(3)).build(),
            EmployeeHistory.builder().employee(lucas).eventType(AuditEventType.STATUS_CHANGE).description("Transitioned status to ON_LEAVE for approved annual leave").timestamp(LocalDateTime.now().minusDays(2)).build(),
            EmployeeHistory.builder().employee(maya).eventType(AuditEventType.HIRED).description("Joined as Staff Security Engineer").timestamp(LocalDateTime.now().minusYears(1).minusMonths(10)).build(),
            EmployeeHistory.builder().employee(maya).eventType(AuditEventType.ROLE_CHANGE).description("Title promoted to Senior Staff Security Engineer").timestamp(LocalDateTime.now().minusMonths(3)).build(),
            EmployeeHistory.builder().employee(liam).eventType(AuditEventType.HIRED).description("Joined DevOps & SRE infrastructure squad").timestamp(LocalDateTime.now().minusDays(25)).build(),
            EmployeeHistory.builder().employee(ryan).eventType(AuditEventType.HIRED).description("Joined Strategic Enterprise Accounts group").timestamp(LocalDateTime.now().minusMonths(5)).build()
        ));

        // 5. Seed Assets
        assetRepository.saveAll(List.of(
            Asset.builder().assetTag("AST-ENG-001").name("Apple MacBook Pro 16\" M3 Max (64GB)").category(AssetCategory.LAPTOP).serialNumber("C02GF012MD6T").assignedDate(LocalDate.now().minusYears(2)).status(AssetStatus.ASSIGNED).assignedEmployee(alex).build(),
            Asset.builder().assetTag("AST-ENG-002").name("Dell UltraSharp 32\" 4K U3223QE").category(AssetCategory.MONITOR).serialNumber("CN-0M35T1-7444").assignedDate(LocalDate.now().minusYears(2)).status(AssetStatus.ASSIGNED).assignedEmployee(alex).build(),
            Asset.builder().assetTag("AST-ENG-003").name("Apple MacBook Pro 14\" M3 Pro (36GB)").category(AssetCategory.LAPTOP).serialNumber("C02HP449KL12").assignedDate(LocalDate.now().minusMonths(10)).status(AssetStatus.ASSIGNED).assignedEmployee(maya).build(),
            Asset.builder().assetTag("AST-ENG-004").name("Dell Precision 7680 Workstation").category(AssetCategory.LAPTOP).serialNumber("8KJG7F3").assignedDate(LocalDate.now().minusMonths(14)).status(AssetStatus.ASSIGNED).assignedEmployee(lucas).build(),
            Asset.builder().assetTag("AST-PRD-001").name("Apple Studio Display 27\" 5K Retina").category(AssetCategory.MONITOR).serialNumber("F2LF8919N16").assignedDate(LocalDate.now().minusMonths(8)).status(AssetStatus.ASSIGNED).assignedEmployee(aisha).build(),
            Asset.builder().assetTag("AST-SEC-001").name("Corporate Smartcard & Dual-Key RFID Badge").category(AssetCategory.ACCESS_CARD).serialNumber("AC-88992-V2").assignedDate(LocalDate.now().minusDays(25)).status(AssetStatus.ASSIGNED).assignedEmployee(liam).build(),
            Asset.builder().assetTag("AST-STK-001").name("Apple MacBook Pro 16\" M3 Max (36GB)").category(AssetCategory.LAPTOP).serialNumber("C02KL8819921").assignedDate(null).status(AssetStatus.IN_STOCK).assignedEmployee(null).build(),
            Asset.builder().assetTag("AST-STK-002").name("Dell UltraSharp 27\" USB-C Hub Monitor").category(AssetCategory.MONITOR).serialNumber("CN-0X984-2199").assignedDate(null).status(AssetStatus.IN_STOCK).assignedEmployee(null).build(),
            Asset.builder().assetTag("AST-STK-003").name("YubiKey 5C NFC Enterprise Token").category(AssetCategory.PERIPHERAL).serialNumber("YK-9912048-A").assignedDate(null).status(AssetStatus.IN_STOCK).assignedEmployee(null).build(),
            Asset.builder().assetTag("AST-REP-001").name("Lenovo ThinkPad X1 Carbon Gen 12").category(AssetCategory.LAPTOP).serialNumber("PF49KLL8").assignedDate(null).status(AssetStatus.UNDER_REPAIR).assignedEmployee(null).build()
        ));

        // 6. Seed Leave Requests
        leaveRequestRepository.saveAll(List.of(
            LeaveRequest.builder()
                .employee(lucas)
                .startDate(LocalDate.now().minusDays(2))
                .endDate(LocalDate.now().plusDays(5))
                .leaveType(LeaveType.ANNUAL)
                .reason("Scheduled family holiday and personal leave")
                .status(LeaveStatus.APPROVED)
                .appliedAt(LocalDateTime.now().minusDays(10))
                .build(),
            LeaveRequest.builder()
                .employee(chloe)
                .startDate(LocalDate.now().plusDays(3))
                .endDate(LocalDate.now().plusDays(4))
                .leaveType(LeaveType.CASUAL)
                .reason("Attending national human resources symposium")
                .status(LeaveStatus.PENDING)
                .appliedAt(LocalDateTime.now().minusDays(1))
                .build(),
            LeaveRequest.builder()
                .employee(liam)
                .startDate(LocalDate.now().plusDays(10))
                .endDate(LocalDate.now().plusDays(12))
                .leaveType(LeaveType.ANNUAL)
                .reason("Post-onboarding break and personal time")
                .status(LeaveStatus.PENDING)
                .appliedAt(LocalDateTime.now().minusHours(6))
                .build(),
            LeaveRequest.builder()
                .employee(ryan)
                .startDate(LocalDate.now().minusDays(15))
                .endDate(LocalDate.now().minusDays(13))
                .leaveType(LeaveType.CASUAL)
                .reason("Personal urgent commitments")
                .status(LeaveStatus.REJECTED)
                .appliedAt(LocalDateTime.now().minusDays(20))
                .build(),
            LeaveRequest.builder()
                .employee(maya)
                .startDate(LocalDate.now().minusMonths(1))
                .endDate(LocalDate.now().minusMonths(1).plusDays(2))
                .leaveType(LeaveType.SICK)
                .reason("Flu recovery and health recuperation")
                .status(LeaveStatus.APPROVED)
                .appliedAt(LocalDateTime.now().minusMonths(1).minusDays(3))
                .build()
        ));

        // 7. Seed Role-Based User Accounts
        appUserRepository.saveAll(List.of(
            AppUser.builder()
                .username("admin")
                .passwordHash(passwordEncoder.encode("admin123"))
                .displayName("Marcus Vance")
                .role(Role.ADMIN)
                .employee(marcus)
                .enabled(true)
                .build(),
            AppUser.builder()
                .username("hr")
                .passwordHash(passwordEncoder.encode("hr123"))
                .displayName("Elena Rostova")
                .role(Role.HR_MANAGER)
                .employee(elena)
                .enabled(true)
                .build(),
            AppUser.builder()
                .username("manager")
                .passwordHash(passwordEncoder.encode("manager123"))
                .displayName("Alexander Wright")
                .role(Role.MANAGER)
                .employee(alex)
                .enabled(true)
                .build(),
            AppUser.builder()
                .username("employee")
                .passwordHash(passwordEncoder.encode("emp123"))
                .displayName("Liam O'Connor")
                .role(Role.EMPLOYEE)
                .employee(liam)
                .enabled(true)
                .build()
        ));

        log.info("Database seeding successfully completed with 5 departments, {} employees, 10 assets, 5 leave records, and 4 RBAC user accounts.",
            employeeRepository.count());
    }
}
