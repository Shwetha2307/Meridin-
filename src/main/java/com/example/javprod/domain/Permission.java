package com.example.javprod.domain;

/**
 * Fine-grained capabilities. Roles are just named bundles of these, and the API
 * authorizes against permissions rather than role names.
 */
public enum Permission {
    /** Create, edit and terminate employees. */
    EMPLOYEE_WRITE,
    /** See salary figures of other employees. */
    SALARY_VIEW,
    /** Approve or reject leave requests (scope-limited for managers). */
    LEAVE_DECIDE,
    /** See every leave request in the organization. */
    LEAVE_VIEW_ALL,
    /** Add, assign, return and repair company assets. */
    ASSET_WRITE,
    /** See the whole asset registry. */
    ASSET_VIEW_ALL,
    /** See the organization-wide activity feed. */
    ACTIVITY_VIEW,
    /** Manage departments, delete assets, open the database console. */
    ORG_ADMIN
}
