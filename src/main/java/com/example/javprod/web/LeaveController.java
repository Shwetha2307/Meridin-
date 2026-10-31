package com.example.javprod.web;

import com.example.javprod.domain.LeaveStatus;
import com.example.javprod.dto.LeaveActionRequest;
import com.example.javprod.dto.LeaveApplicationRequest;
import com.example.javprod.dto.LeaveResponse;
import com.example.javprod.service.LeaveService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leaves")
@RequiredArgsConstructor
public class LeaveController {

    private final LeaveService leaveService;

    @GetMapping
    public ResponseEntity<List<LeaveResponse>> getLeaves(
        @RequestParam(name = "status", required = false) LeaveStatus status,
        @RequestParam(name = "employeeId", required = false) Long employeeId
    ) {
        return ResponseEntity.ok(leaveService.getLeaves(status, employeeId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LeaveResponse> getLeaveById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(leaveService.getLeaveById(id));
    }

    @PostMapping
    public ResponseEntity<LeaveResponse> applyLeave(@Valid @RequestBody LeaveApplicationRequest request) {
        LeaveResponse response = leaveService.applyLeave(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{id}/approve")
    @PreAuthorize("hasAuthority('LEAVE_DECIDE') or hasAnyRole('ADMIN', 'HR_MANAGER', 'MANAGER')")
    public ResponseEntity<LeaveResponse> approveLeave(
        @PathVariable("id") Long id,
        @RequestBody(required = false) LeaveActionRequest request
    ) {
        LeaveResponse response = leaveService.approveLeave(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/reject")
    @PreAuthorize("hasAuthority('LEAVE_DECIDE') or hasAnyRole('ADMIN', 'HR_MANAGER', 'MANAGER')")
    public ResponseEntity<LeaveResponse> rejectLeave(
        @PathVariable("id") Long id,
        @RequestBody(required = false) LeaveActionRequest request
    ) {
        LeaveResponse response = leaveService.rejectLeave(id, request);
        return ResponseEntity.ok(response);
    }
}
