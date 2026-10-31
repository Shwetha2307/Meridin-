package com.example.javprod.domain;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import static com.example.javprod.domain.Permission.*;

public enum Role {

    ADMIN("Administrator", EnumSet.allOf(Permission.class)),

    HR_MANAGER("HR Manager", EnumSet.of(
        EMPLOYEE_WRITE, SALARY_VIEW, LEAVE_DECIDE, LEAVE_VIEW_ALL, ASSET_WRITE, ASSET_VIEW_ALL, ACTIVITY_VIEW)),

    MANAGER("Manager", EnumSet.of(LEAVE_DECIDE, ASSET_VIEW_ALL, ACTIVITY_VIEW)),

    EMPLOYEE("Employee", EnumSet.noneOf(Permission.class));

    private final String label;
    private final Set<Permission> permissions;

    Role(String label, Set<Permission> permissions) {
        this.label = label;
        this.permissions = Collections.unmodifiableSet(permissions);
    }

    public String label() {
        return label;
    }

    public String getLabel() {
        return label;
    }

    public Set<Permission> permissions() {
        return permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }
}
