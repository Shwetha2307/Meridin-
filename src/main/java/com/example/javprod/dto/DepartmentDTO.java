package com.example.javprod.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DepartmentDTO(
    Long id,
    @NotBlank(message = "Department name is required")
    @Size(max = 100, message = "Department name must not exceed 100 characters")
    String name,

    @NotBlank(message = "Department code is required")
    @Size(max = 20, message = "Department code must not exceed 20 characters")
    String code,

    @Size(max = 500, message = "Description must not exceed 500 characters")
    String description,

    long employeeCount
) {
    public DepartmentDTO(Long id, String name, String code, String description) {
        this(id, name, code, description, 0);
    }
}
