package com.example.javprod.dto;

import com.example.javprod.domain.AppUser;
import com.example.javprod.domain.Employee;
import com.example.javprod.domain.Permission;
import com.example.javprod.domain.Role;

import java.util.Set;
import java.util.stream.Collectors;

public record AuthUserDTO(
    Long id,
    String username,
    String displayName,
    Role role,
    String roleLabel,
    Long employeeId,
    String email,
    String jobTitle,
    String departmentName,
    Set<String> permissions
) {
    public static AuthUserDTO fromEntity(AppUser user) {
        Employee emp = user.getEmployee();
        Set<String> perms = user.getRole().getPermissions().stream()
            .map(Permission::name)
            .collect(Collectors.toSet());

        return new AuthUserDTO(
            user.getId(),
            user.getUsername(),
            user.getDisplayName(),
            user.getRole(),
            user.getRole().getLabel(),
            emp != null ? emp.getId() : null,
            emp != null ? emp.getEmail() : user.getUsername() + "@enterprise.io",
            emp != null ? emp.getRole() : user.getRole().getLabel(),
            emp != null && emp.getDepartment() != null ? emp.getDepartment().getName() : "Corporate Administration",
            perms
        );
    }
}
