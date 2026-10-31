package com.example.javprod.service;

import com.example.javprod.domain.LeaveStatus;
import com.example.javprod.dto.LeaveActionRequest;
import com.example.javprod.dto.LeaveApplicationRequest;
import com.example.javprod.dto.LeaveResponse;

import java.util.List;

public interface LeaveService {
    List<LeaveResponse> getLeaves(LeaveStatus status, Long employeeId);
    LeaveResponse getLeaveById(Long id);
    LeaveResponse applyLeave(LeaveApplicationRequest request);
    LeaveResponse approveLeave(Long id, LeaveActionRequest request);
    LeaveResponse rejectLeave(Long id, LeaveActionRequest request);
}
