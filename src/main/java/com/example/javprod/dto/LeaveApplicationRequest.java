package com.example.javprod.dto;

import com.example.javprod.domain.LeaveType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record LeaveApplicationRequest(
    @NotNull(message = "Employee ID is required")
    Long employeeId,

    @NotNull(message = "Start date is required")
    LocalDate startDate,

    @NotNull(message = "End date is required")
    LocalDate endDate,

    @NotNull(message = "Leave type is required")
    LeaveType leaveType,

    @NotBlank(message = "Reason is required")
    String reason
) {}
