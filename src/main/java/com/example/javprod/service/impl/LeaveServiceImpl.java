package com.example.javprod.service.impl;

import com.example.javprod.domain.*;
import com.example.javprod.dto.LeaveActionRequest;
import com.example.javprod.dto.LeaveApplicationRequest;
import com.example.javprod.dto.LeaveResponse;
import com.example.javprod.exception.InvalidOperationException;
import com.example.javprod.exception.ResourceNotFoundException;
import com.example.javprod.repository.EmployeeRepository;
import com.example.javprod.repository.LeaveRequestRepository;
import com.example.javprod.service.EmployeeHistoryService;
import com.example.javprod.service.LeaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LeaveServiceImpl implements LeaveService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final EmployeeRepository employeeRepository;
    private final EmployeeHistoryService historyService;

    @Override
    public List<LeaveResponse> getLeaves(LeaveStatus status, Long employeeId) {
        List<LeaveRequest> list;
        if (employeeId != null) {
            list = leaveRequestRepository.findByEmployeeIdWithDetails(employeeId);
        } else if (status != null) {
            list = leaveRequestRepository.findByStatusWithDetails(status);
        } else {
            list = leaveRequestRepository.findAllWithDetails();
        }
        return list.stream().map(LeaveResponse::fromEntity).toList();
    }

    @Override
    public LeaveResponse getLeaveById(Long id) {
        LeaveRequest req = leaveRequestRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Leave request not found with ID: " + id));
        return LeaveResponse.fromEntity(req);
    }

    @Override
    @Transactional
    public LeaveResponse applyLeave(LeaveApplicationRequest req) {
        if (req.endDate().isBefore(req.startDate())) {
            throw new InvalidOperationException("End date (" + req.endDate() + ") cannot precede start date (" + req.startDate() + ").");
        }

        Employee employee = employeeRepository.findById(req.employeeId())
            .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + req.employeeId()));

        if (employee.getStatus() == EmployeeStatus.TERMINATED) {
            throw new InvalidOperationException("Cannot apply leave for a terminated employee.");
        }

        if (leaveRequestRepository.existsOverlappingLeave(employee.getId(), req.startDate(), req.endDate())) {
            throw new InvalidOperationException("This leave overlaps with an existing pending or approved request.");
        }

        LeaveRequest leaveRequest = LeaveRequest.builder()
            .employee(employee)
            .startDate(req.startDate())
            .endDate(req.endDate())
            .leaveType(req.leaveType())
            .reason(req.reason().trim())
            .status(LeaveStatus.PENDING)
            .appliedAt(LocalDateTime.now())
            .build();

        LeaveRequest saved = leaveRequestRepository.save(leaveRequest);
        return LeaveResponse.fromEntity(saved);
    }

    @Override
    @Transactional
    public LeaveResponse approveLeave(Long id, LeaveActionRequest request) {
        LeaveRequest leave = leaveRequestRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Leave request not found with ID: " + id));

        if (leave.getStatus() != LeaveStatus.PENDING) {
            throw new InvalidOperationException("Cannot approve a leave request that is already " + leave.getStatus());
        }

        leave.setStatus(LeaveStatus.APPROVED);
        LeaveRequest updated = leaveRequestRepository.save(leave);

        Employee employee = leave.getEmployee();
        LocalDate today = LocalDate.now();
        // If leave covers today, transition employee status to ON_LEAVE
        if (!today.isBefore(leave.getStartDate()) && !today.isAfter(leave.getEndDate())) {
            employee.setStatus(EmployeeStatus.ON_LEAVE);
            employeeRepository.save(employee);
        }

        String remarks = (request != null && request.remarks() != null && !request.remarks().isBlank())
            ? " (" + request.remarks().trim() + ")"
            : "";

        historyService.recordEvent(
            employee,
            AuditEventType.STATUS_CHANGE,
            String.format("Approved %s leave for %s to %s%s", leave.getLeaveType(), leave.getStartDate(), leave.getEndDate(), remarks)
        );

        return LeaveResponse.fromEntity(updated);
    }

    @Override
    @Transactional
    public LeaveResponse rejectLeave(Long id, LeaveActionRequest request) {
        LeaveRequest leave = leaveRequestRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Leave request not found with ID: " + id));

        if (leave.getStatus() != LeaveStatus.PENDING) {
            throw new InvalidOperationException("Cannot reject a leave request that is already " + leave.getStatus());
        }

        leave.setStatus(LeaveStatus.REJECTED);
        LeaveRequest updated = leaveRequestRepository.save(leave);

        String remarks = (request != null && request.remarks() != null && !request.remarks().isBlank())
            ? " (" + request.remarks().trim() + ")"
            : "";

        historyService.recordEvent(
            leave.getEmployee(),
            AuditEventType.STATUS_CHANGE,
            String.format("Rejected %s leave application%s", leave.getLeaveType(), remarks)
        );

        return LeaveResponse.fromEntity(updated);
    }
}
