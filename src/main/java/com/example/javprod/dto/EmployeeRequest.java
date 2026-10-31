package com.example.javprod.dto;

import com.example.javprod.domain.EmployeeStatus;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EmployeeRequest(
    @NotBlank(message = "First name is required")
    @Size(max = 60, message = "First name cannot exceed 60 characters")
    String firstName,

    @NotBlank(message = "Last name is required")
    @Size(max = 60, message = "Last name cannot exceed 60 characters")
    String lastName,

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    String email,

    @NotBlank(message = "Phone number is required")
    String phone,

    @NotBlank(message = "Role is required")
    @Size(max = 100, message = "Role cannot exceed 100 characters")
    String role,

    @NotNull(message = "Hire date is required")
    @PastOrPresent(message = "Hire date cannot be in the future")
    LocalDate hireDate,

    @NotNull(message = "Salary is required")
    @Positive(message = "Salary must be positive")
    BigDecimal salary,

    @NotNull(message = "Status is required")
    EmployeeStatus status,

    @NotNull(message = "Department ID is required")
    Long departmentId,

    Long managerId
) {}
