package com.example.javprod.dto;

import java.util.List;

public record EmployeeHierarchyDTO(
    EmployeeResponse employee,
    EmployeeResponse manager,
    List<EmployeeResponse> directReports
) {}
