package com.example.javprod.service.impl;

import com.example.javprod.domain.AuditEventType;
import com.example.javprod.domain.Employee;
import com.example.javprod.domain.EmployeeHistory;
import com.example.javprod.dto.TimelineEventDTO;
import com.example.javprod.repository.EmployeeHistoryRepository;
import com.example.javprod.service.EmployeeHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmployeeHistoryServiceImpl implements EmployeeHistoryService {

    private final EmployeeHistoryRepository employeeHistoryRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void recordEvent(Employee employee, AuditEventType eventType, String description) {
        if (employee == null) return;
        EmployeeHistory history = EmployeeHistory.builder()
            .employee(employee)
            .eventType(eventType)
            .description(description)
            .timestamp(LocalDateTime.now())
            .build();
        employeeHistoryRepository.save(history);
    }

    @Override
    public List<TimelineEventDTO> getRecentActivities() {
        return employeeHistoryRepository.findRecentActivities().stream()
            .map(TimelineEventDTO::fromEntity)
            .toList();
    }

    @Override
    public List<TimelineEventDTO> getEmployeeHistory(Long employeeId) {
        return employeeHistoryRepository.findByEmployeeId(employeeId).stream()
            .map(TimelineEventDTO::fromEntity)
            .toList();
    }
}
