package com.example.javprod.service;

import com.example.javprod.domain.EmployeeStatus;
import com.example.javprod.dto.EmployeeHierarchyDTO;
import com.example.javprod.dto.EmployeeRequest;
import com.example.javprod.dto.EmployeeResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmployeeService {
    Page<EmployeeResponse> searchEmployees(String query, Long departmentId, EmployeeStatus status, Pageable pageable);
    List<EmployeeResponse> getAllEmployeesList();
    EmployeeResponse getEmployeeById(Long id);
    EmployeeHierarchyDTO getEmployeeHierarchy(Long id);
    EmployeeResponse createEmployee(EmployeeRequest request);
    EmployeeResponse updateEmployee(Long id, EmployeeRequest request);
    void deleteEmployee(Long id);
}
