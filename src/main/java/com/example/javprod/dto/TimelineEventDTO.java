package com.example.javprod.dto;

import com.example.javprod.domain.AuditEventType;
import com.example.javprod.domain.EmployeeHistory;

import java.time.LocalDateTime;

public record TimelineEventDTO(
    Long id,
    Long employeeId,
    String employeeName,
    AuditEventType eventType,
    String description,
    LocalDateTime timestamp
) {
    public static TimelineEventDTO fromEntity(EmployeeHistory history) {
        if (history == null) return null;
        String empName = history.getEmployee() != null 
            ? history.getEmployee().getFirstName() + " " + history.getEmployee().getLastName()
            : "Unknown";
        Long empId = history.getEmployee() != null ? history.getEmployee().getId() : null;

        return new TimelineEventDTO(
            history.getId(),
            empId,
            empName,
            history.getEventType(),
            history.getDescription(),
            history.getTimestamp()
        );
    }
}
