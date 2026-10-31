package com.example.javprod.dto;

import com.example.javprod.domain.Employee;
import com.example.javprod.domain.EmployeeStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EmployeeResponse(
    Long id,
    String firstName,
    String lastName,
    String fullName,
    String email,
    String phone,
    String role,
    LocalDate hireDate,
    BigDecimal salary,
    EmployeeStatus status,
    Long departmentId,
    String departmentName,
    String departmentCode,
    Long managerId,
    String managerName
) {
    public static EmployeeResponse fromEntity(Employee emp) {
        if (emp == null) return null;
        String fullName = emp.getFirstName() + " " + emp.getLastName();
        String deptName = emp.getDepartment() != null ? emp.getDepartment().getName() : null;
        String deptCode = emp.getDepartment() != null ? emp.getDepartment().getCode() : null;
        Long deptId = emp.getDepartment() != null ? emp.getDepartment().getId() : null;

        Long mgrId = emp.getManager() != null ? emp.getManager().getId() : null;
        String mgrName = emp.getManager() != null 
            ? emp.getManager().getFirstName() + " " + emp.getManager().getLastName() 
            : null;

        return new EmployeeResponse(
            emp.getId(),
            emp.getFirstName(),
            emp.getLastName(),
            fullName,
            emp.getEmail(),
            emp.getPhone(),
            emp.getRole(),
            emp.getHireDate(),
            emp.getSalary(),
            emp.getStatus(),
            deptId,
            deptName,
            deptCode,
            mgrId,
            mgrName
        );
    }

    public EmployeeResponse withRedactedSalary() {
        return new EmployeeResponse(
            id, firstName, lastName, fullName, email, phone, role,
            hireDate, null, status, departmentId, departmentName,
            departmentCode, managerId, managerName
        );
    }
}
