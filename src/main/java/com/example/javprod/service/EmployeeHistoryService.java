package com.example.javprod.service;

import com.example.javprod.domain.AuditEventType;
import com.example.javprod.domain.Employee;
import com.example.javprod.dto.TimelineEventDTO;

import java.util.List;

public interface EmployeeHistoryService {
    void recordEvent(Employee employee, AuditEventType eventType, String description);
    List<TimelineEventDTO> getRecentActivities();
    List<TimelineEventDTO> getEmployeeHistory(Long employeeId);
}
