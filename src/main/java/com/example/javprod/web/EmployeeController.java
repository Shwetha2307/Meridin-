package com.example.javprod.web;

import com.example.javprod.domain.AppUser;
import com.example.javprod.domain.EmployeeStatus;
import com.example.javprod.dto.EmployeeHierarchyDTO;
import com.example.javprod.dto.EmployeeRequest;
import com.example.javprod.dto.EmployeeResponse;
import com.example.javprod.repository.AppUserRepository;
import com.example.javprod.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private static final Set<String> SORTABLE_FIELDS =
        Set.of("id", "firstName", "lastName", "email", "role", "hireDate", "salary", "status");

    private final EmployeeService employeeService;
    private final AppUserRepository appUserRepository;

    @GetMapping
    public ResponseEntity<Page<EmployeeResponse>> getEmployees(
        @RequestParam(name = "search", required = false) String search,
        @RequestParam(name = "departmentId", required = false) Long departmentId,
        @RequestParam(name = "status", required = false) EmployeeStatus status,
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "10") int size,
        @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
        @RequestParam(name = "sortDir", defaultValue = "asc") String sortDir,
        Authentication authentication
    ) {
        String safeSortBy = SORTABLE_FIELDS.contains(sortBy) ? sortBy : "id";
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(safeSortBy).descending() : Sort.by(safeSortBy).ascending();
        Pageable pageable = PageRequest.of(Math.max(0, page), Math.max(1, size), sort);
        boolean canViewSalary = hasSalaryViewPermission(authentication);
        Long selfEmployeeId = resolveSelfEmployeeId(authentication);

        Page<EmployeeResponse> result = employeeService.searchEmployees(search, departmentId, status, pageable)
            .map(emp -> sanitizeSalary(emp, canViewSalary, selfEmployeeId));
        return ResponseEntity.ok(result);
    }

    @GetMapping("/list")
    public ResponseEntity<List<EmployeeResponse>> getAllEmployeesList(Authentication authentication) {
        boolean canViewSalary = hasSalaryViewPermission(authentication);
        Long selfEmployeeId = resolveSelfEmployeeId(authentication);
        List<EmployeeResponse> list = employeeService.getAllEmployeesList().stream()
            .map(emp -> sanitizeSalary(emp, canViewSalary, selfEmployeeId))
            .toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> getEmployeeById(@PathVariable("id") Long id, Authentication authentication) {
        boolean canViewSalary = hasSalaryViewPermission(authentication);
        Long selfEmployeeId = resolveSelfEmployeeId(authentication);
        EmployeeResponse emp = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(sanitizeSalary(emp, canViewSalary, selfEmployeeId));
    }

    @GetMapping("/{id}/hierarchy")
    public ResponseEntity<EmployeeHierarchyDTO> getEmployeeHierarchy(@PathVariable("id") Long id, Authentication authentication) {
        boolean canViewSalary = hasSalaryViewPermission(authentication);
        Long selfEmployeeId = resolveSelfEmployeeId(authentication);
        EmployeeHierarchyDTO dto = employeeService.getEmployeeHierarchy(id);
        EmployeeHierarchyDTO sanitized = new EmployeeHierarchyDTO(
            sanitizeSalary(dto.employee(), canViewSalary, selfEmployeeId),
            sanitizeSalary(dto.manager(), canViewSalary, selfEmployeeId),
            dto.directReports() != null
                ? dto.directReports().stream().map(r -> sanitizeSalary(r, canViewSalary, selfEmployeeId)).toList()
                : List.of()
        );
        return ResponseEntity.ok(sanitized);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('EMPLOYEE_WRITE') or hasAnyRole('ADMIN', 'HR_MANAGER')")
    public ResponseEntity<EmployeeResponse> createEmployee(@Valid @RequestBody EmployeeRequest request) {
        EmployeeResponse created = employeeService.createEmployee(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('EMPLOYEE_WRITE') or hasAnyRole('ADMIN', 'HR_MANAGER')")
    public ResponseEntity<EmployeeResponse> updateEmployee(
        @PathVariable("id") Long id,
        @Valid @RequestBody EmployeeRequest request
    ) {
        EmployeeResponse updated = employeeService.updateEmployee(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('EMPLOYEE_WRITE') or hasAnyRole('ADMIN', 'HR_MANAGER')")
    public ResponseEntity<Void> deleteEmployee(@PathVariable("id") Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

    private boolean hasSalaryViewPermission(Authentication authentication) {
        if (authentication == null) return false;
        Set<String> auths = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toSet());
        return auths.contains("SALARY_VIEW")
            || auths.contains("ROLE_ADMIN")
            || auths.contains("ROLE_HR_MANAGER");
    }

    private Long resolveSelfEmployeeId(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) return null;
        return appUserRepository.findByUsernameIgnoreCase(authentication.getName())
            .map(AppUser::getEmployee)
            .map(e -> e != null ? e.getId() : null)
            .orElse(null);
    }

    private EmployeeResponse sanitizeSalary(EmployeeResponse emp, boolean canViewSalary, Long selfEmployeeId) {
        if (emp == null || canViewSalary) return emp;
        if (selfEmployeeId != null && selfEmployeeId.equals(emp.id())) return emp;
        return emp.withRedactedSalary();
    }
}
