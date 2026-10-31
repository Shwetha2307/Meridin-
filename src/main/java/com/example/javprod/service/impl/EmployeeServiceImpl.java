package com.example.javprod.service.impl;

import com.example.javprod.domain.AuditEventType;
import com.example.javprod.domain.Department;
import com.example.javprod.domain.Employee;
import com.example.javprod.domain.EmployeeStatus;
import com.example.javprod.dto.EmployeeHierarchyDTO;
import com.example.javprod.dto.EmployeeRequest;
import com.example.javprod.dto.EmployeeResponse;
import com.example.javprod.exception.DuplicateResourceException;
import com.example.javprod.exception.InvalidOperationException;
import com.example.javprod.exception.ResourceNotFoundException;
import com.example.javprod.repository.DepartmentRepository;
import com.example.javprod.repository.EmployeeRepository;
import com.example.javprod.service.EmployeeHistoryService;
import com.example.javprod.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeHistoryService historyService;

    @Override
    public Page<EmployeeResponse> searchEmployees(String query, Long departmentId, EmployeeStatus status, Pageable pageable) {
        String trimmedQuery = (query != null && !query.trim().isEmpty()) ? query.trim() : null;
        return employeeRepository.searchEmployees(trimmedQuery, departmentId, status, pageable)
            .map(EmployeeResponse::fromEntity);
    }

    @Override
    public List<EmployeeResponse> getAllEmployeesList() {
        return employeeRepository.findAll().stream()
            .map(EmployeeResponse::fromEntity)
            .toList();
    }

    @Override
    public EmployeeResponse getEmployeeById(Long id) {
        Employee emp = employeeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));
        return EmployeeResponse.fromEntity(emp);
    }

    @Override
    public EmployeeHierarchyDTO getEmployeeHierarchy(Long id) {
        Employee emp = employeeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));

        EmployeeResponse empResp = EmployeeResponse.fromEntity(emp);
        EmployeeResponse mgrResp = emp.getManager() != null ? EmployeeResponse.fromEntity(emp.getManager()) : null;
        List<EmployeeResponse> directReports = employeeRepository.findByManagerId(id).stream()
            .map(EmployeeResponse::fromEntity)
            .toList();

        return new EmployeeHierarchyDTO(empResp, mgrResp, directReports);
    }

    @Override
    @Transactional
    public EmployeeResponse createEmployee(EmployeeRequest req) {
        String email = req.email().trim().toLowerCase();
        if (employeeRepository.existsByEmail(email)) {
            throw new DuplicateResourceException("An employee with email '" + email + "' already exists.");
        }

        Department department = departmentRepository.findById(req.departmentId())
            .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + req.departmentId()));

        Employee manager = null;
        if (req.managerId() != null) {
            manager = employeeRepository.findById(req.managerId())
                .orElseThrow(() -> new ResourceNotFoundException("Manager not found with ID: " + req.managerId()));
        }

        Employee employee = Employee.builder()
            .firstName(req.firstName().trim())
            .lastName(req.lastName().trim())
            .email(email)
            .phone(req.phone().trim())
            .role(req.role().trim())
            .hireDate(req.hireDate())
            .salary(req.salary())
            .status(req.status() != null ? req.status() : EmployeeStatus.ACTIVE)
            .department(department)
            .manager(manager)
            .build();

        Employee saved = employeeRepository.save(employee);

        // Record automatic audit event for hiring
        historyService.recordEvent(
            saved,
            AuditEventType.HIRED,
            String.format("Joined %s as %s (Reporting to: %s)",
                department.getName(),
                saved.getRole(),
                manager != null ? manager.getFirstName() + " " + manager.getLastName() : "Executive Leadership")
        );

        return EmployeeResponse.fromEntity(saved);
    }

    @Override
    @Transactional
    public EmployeeResponse updateEmployee(Long id, EmployeeRequest req) {
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));

        String email = req.email().trim().toLowerCase();
        if (employeeRepository.existsByEmailAndIdNot(email, id)) {
            throw new DuplicateResourceException("An employee with email '" + email + "' already exists.");
        }

        Department newDept = departmentRepository.findById(req.departmentId())
            .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + req.departmentId()));

        Employee newManager = null;
        if (req.managerId() != null) {
            if (req.managerId().equals(id)) {
                throw new InvalidOperationException("An employee cannot be their own manager.");
            }
            newManager = employeeRepository.findById(req.managerId())
                .orElseThrow(() -> new ResourceNotFoundException("Manager not found with ID: " + req.managerId()));
        }

        // Detect audit changes
        boolean deptChanged = !employee.getDepartment().getId().equals(newDept.getId());
        boolean roleChanged = !employee.getRole().equalsIgnoreCase(req.role().trim());
        boolean statusChanged = employee.getStatus() != req.status();
        boolean salaryChanged = employee.getSalary().compareTo(req.salary()) != 0;

        String oldRole = employee.getRole();
        String oldDeptName = employee.getDepartment().getName();
        EmployeeStatus oldStatus = employee.getStatus();
        BigDecimal oldSalary = employee.getSalary();

        // Apply updates
        employee.setFirstName(req.firstName().trim());
        employee.setLastName(req.lastName().trim());
        employee.setEmail(email);
        employee.setPhone(req.phone().trim());
        employee.setRole(req.role().trim());
        employee.setHireDate(req.hireDate());
        employee.setSalary(req.salary());
        employee.setStatus(req.status());
        employee.setDepartment(newDept);
        employee.setManager(newManager);

        Employee updated = employeeRepository.save(employee);

        // Record audit events
        if (deptChanged) {
            historyService.recordEvent(
                updated,
                AuditEventType.TRANSFERRED,
                String.format("Transferred from %s to %s", oldDeptName, newDept.getName())
            );
        }

        if (roleChanged) {
            historyService.recordEvent(
                updated,
                AuditEventType.ROLE_CHANGE,
                String.format("Role updated from '%s' to '%s'", oldRole, updated.getRole())
            );
        }

        if (salaryChanged && req.salary().compareTo(oldSalary) > 0) {
            historyService.recordEvent(
                updated,
                AuditEventType.PROMOTED,
                String.format("Compensation revised from $%s to $%s", oldSalary.toPlainString(), req.salary().toPlainString())
            );
        }

        if (statusChanged) {
            historyService.recordEvent(
                updated,
                req.status() == EmployeeStatus.TERMINATED ? AuditEventType.TERMINATED : AuditEventType.STATUS_CHANGE,
                String.format("Employment status transitioned from %s to %s", oldStatus, req.status())
            );
        }

        return EmployeeResponse.fromEntity(updated);
    }

    @Override
    @Transactional
    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));

        // Reassign direct reports to employee's manager before termination/deletion
        List<Employee> reports = employeeRepository.findByManagerId(id);
        for (Employee report : reports) {
            report.setManager(employee.getManager());
            employeeRepository.save(report);
        }

        // Set status to TERMINATED and record history
        employee.setStatus(EmployeeStatus.TERMINATED);
        employeeRepository.save(employee);

        historyService.recordEvent(
            employee,
            AuditEventType.TERMINATED,
            "Employee contract terminated. Direct reports reassigned to organizational hierarchy."
        );
    }
}
