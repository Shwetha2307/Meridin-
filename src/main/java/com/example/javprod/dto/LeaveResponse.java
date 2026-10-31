package com.example.javprod.dto;

import com.example.javprod.domain.LeaveRequest;
import com.example.javprod.domain.LeaveStatus;
import com.example.javprod.domain.LeaveType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public record LeaveResponse(
    Long id,
    Long employeeId,
    String employeeName,
    String employeeEmail,
    String departmentName,
    LocalDate startDate,
    LocalDate endDate,
    long daysCount,
    LeaveType leaveType,
    String reason,
    LeaveStatus status,
    LocalDateTime appliedAt
) {
    public static LeaveResponse fromEntity(LeaveRequest req) {
        if (req == null) return null;
        String empName = req.getEmployee() != null 
            ? req.getEmployee().getFirstName() + " " + req.getEmployee().getLastName() 
            : "Unknown";
        String empEmail = req.getEmployee() != null ? req.getEmployee().getEmail() : null;
        String deptName = (req.getEmployee() != null && req.getEmployee().getDepartment() != null)
            ? req.getEmployee().getDepartment().getName()
            : null;
        Long empId = req.getEmployee() != null ? req.getEmployee().getId() : null;

        long days = (req.getStartDate() != null && req.getEndDate() != null)
            ? ChronoUnit.DAYS.between(req.getStartDate(), req.getEndDate()) + 1
            : 0;

        return new LeaveResponse(
            req.getId(),
            empId,
            empName,
            empEmail,
            deptName,
            req.getStartDate(),
            req.getEndDate(),
            Math.max(1, days),
            req.getLeaveType(),
            req.getReason(),
            req.getStatus(),
            req.getAppliedAt()
        );
    }
}
